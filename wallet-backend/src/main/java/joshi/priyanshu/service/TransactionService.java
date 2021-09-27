package joshi.priyanshu.service;

import joshi.priyanshu.returnResponse.TransactionReturnResponse;
import joshi.priyanshu.model.Transaction;
import joshi.priyanshu.model.User;
import joshi.priyanshu.repository.TransactionRepository;
import joshi.priyanshu.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    final static String debit = "DEBIT";
    final static String creditByTransfer = "CREDIT_BY_TRANSFER";
    final static String creditByRecharge = "CREDIT_BY_RECHARGE";
    final static String creditByCashback = "CREDIT_BY_CASHBACK";


    public TransactionReturnResponse transferWalletAmountImpl(Transaction transaction) {
        User user = userRepository.findByUserEmail(transaction.getUserEmail());
        User receiver = userRepository.findByUserEmail(transaction.getReceiverEmail());
        if(receiver == null) {
            return TransactionReturnResponse.RECEIVER_NOT_FOUND;
        }
        if(user == null || transaction.getUserEmail().equals(transaction.getReceiverEmail())
                || !user.isActive() || transaction.getTransactionAmount() <= 0) {
            return TransactionReturnResponse.TRANSACTION_FAILED;
        }
        if(transaction.getTransactionType().equals(debit)) {
            if(user.getBalance() < transaction.getTransactionAmount()){
                return TransactionReturnResponse.TRANSACTION_FAILED;
            }
            double senderBalance = user.getBalance() - transaction.getTransactionAmount();
            double receiverBalance = receiver.getBalance() + transaction.getTransactionAmount();
            String debitTransactionNote = "Debit of ₹" + transaction.getTransactionAmount() + " sent to "
                    + transaction.getReceiverEmail();
            String creditTransactionNote = "Credit of ₹" + transaction.getTransactionAmount() + " sent by "
                    + transaction.getUserEmail();
            //Saving user and receiver balance
            user.setBalance(senderBalance);
            receiver.setBalance(receiverBalance);
            userRepository.save(user);
            userRepository.save(receiver);
            //Saving transactions
            transaction.setTransactionID(UUID.randomUUID());
            transaction.setTransactionDateTime(LocalDateTime.now());
            transaction.setTransactionNote(debitTransactionNote);
            transaction.setBalanceAfterTransaction(senderBalance);
            transactionRepository.save(transaction);
            //Receiver transaction
            Transaction transactionForReceiver = getTransactionForReceiver(transaction, creditTransactionNote,
                    receiverBalance);
            transactionRepository.save(transactionForReceiver);
            return TransactionReturnResponse.TRANSACTION_SUCCESS;
        }
        return TransactionReturnResponse.TRANSACTION_FAILED;
    }

    private Transaction getTransactionForReceiver(Transaction transaction, String creditTransactionNote,
                                                  double receiverBalance) {
        Transaction transactionForReceiver = new Transaction();
        transactionForReceiver.setTransactionID(transaction.getTransactionID());
        transactionForReceiver.setUserEmail(transaction.getReceiverEmail());
        transactionForReceiver.setSenderEmail(transaction.getUserEmail());
        transactionForReceiver.setTransactionAmount(transaction.getTransactionAmount());
        transactionForReceiver.setTransactionType(creditByTransfer);
        transactionForReceiver.setTransactionDateTime(transaction.getTransactionDateTime());
        transactionForReceiver.setTransactionNote(creditTransactionNote);
        transactionForReceiver.setBalanceAfterTransaction(receiverBalance);
        return transactionForReceiver;
    }

    public TransactionReturnResponse rechargeWalletImpl(Transaction transaction) {
        User user = userRepository.findByUserEmail(transaction.getUserEmail());
        if(user == null || transaction.getTransactionAmount() <= 0 || !user.isActive()) {
            return TransactionReturnResponse.TRANSACTION_FAILED;
        }
        if (transaction.getTransactionType().equals(creditByRecharge)) {
            if (transaction.getTransactionAmount() < 100) {
            double userBalance = user.getBalance() + transaction.getTransactionAmount();
            String transactionNote = "Credit of ₹" + transaction.getTransactionAmount() + " by Recharge";
            //Saving user balance
            user.setBalance(userBalance);
            userRepository.save(user);
            //Saving transaction
            transaction.setTransactionID(UUID.randomUUID());
            transaction.setTransactionDateTime(LocalDateTime.now());
            transaction.setTransactionNote(transactionNote);
            transaction.setBalanceAfterTransaction(userBalance);
            transactionRepository.save(transaction);
            return TransactionReturnResponse.TRANSACTION_SUCCESS;
        }
        if (transaction.getTransactionAmount() >= 100) {
            double userBalance = user.getBalance() + transaction.getTransactionAmount();
            double userBalanceAfterCashback = user.getBalance() + transaction.getTransactionAmount()
                    + 0.1 * transaction.getTransactionAmount();
            String transactionNote = "Credit of ₹" + transaction.getTransactionAmount() + " by Recharge";
            String cashbackTransactionNote = "Credit of ₹" + 0.1 * transaction.getTransactionAmount() + " by Cashback";
            //Saving user balance
            user.setBalance(userBalanceAfterCashback);
            userRepository.save(user);
            //Saving transaction
            transaction.setTransactionID(UUID.randomUUID());
            transaction.setTransactionDateTime(LocalDateTime.now());
            transaction.setTransactionNote(transactionNote);
            transaction.setBalanceAfterTransaction(userBalance);
            transactionRepository.save(transaction);
            //Saving Cashback transaction
            Transaction transactionForCashback = getTransactionForCashback(transaction, userBalanceAfterCashback,
                    cashbackTransactionNote);
            transactionRepository.save(transactionForCashback);
            return TransactionReturnResponse.TRANSACTION_SUCCESS;
        }
        }
        return TransactionReturnResponse.TRANSACTION_FAILED;
    }

    private Transaction getTransactionForCashback(Transaction transaction, double userBalanceAfterCashback,
                                                  String cashbackTransactionNote) {
        Transaction transactionForCashback = new Transaction();
        transactionForCashback.setTransactionID(UUID.randomUUID());
        transactionForCashback.setUserEmail(transaction.getUserEmail());
        transactionForCashback.setTransactionAmount(0.1 * transaction.getTransactionAmount());
        transactionForCashback.setTransactionType(creditByCashback);
        transactionForCashback.setTransactionDateTime(transaction.getTransactionDateTime());
        transactionForCashback.setTransactionNote(cashbackTransactionNote);
        transactionForCashback.setBalanceAfterTransaction(userBalanceAfterCashback);
        return transactionForCashback;
    }

    public List<Transaction> viewTransactionsImpl(String userEmail) {
        List<Transaction> transactions = transactionRepository.findByUserEmail(userEmail);
        if(transactions.isEmpty()){
            return new ArrayList<>();
        } else {
            return transactions;
        }
    }
}