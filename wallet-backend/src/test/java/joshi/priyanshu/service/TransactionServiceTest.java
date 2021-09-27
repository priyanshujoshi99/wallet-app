package joshi.priyanshu.service;

import joshi.priyanshu.model.Transaction;
import joshi.priyanshu.model.User;
import joshi.priyanshu.repository.TransactionRepository;
import joshi.priyanshu.repository.UserRepository;
import joshi.priyanshu.returnResponse.TransactionReturnResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Autowired
    @InjectMocks
    TransactionService transactionService;

    @Mock
    TransactionRepository transactionRepository;
    @Mock
    UserRepository userRepository;

    @Nested
    @DisplayName("Tests For Wallet Amount Transfer")
    class testTransferWalletAmount {
        @Test
        @DisplayName("Test For Successful Wallet Amount Transfer")
        void testTransferWalletSuccessful() {
            Transaction debitTransaction = new Transaction("1",UUID.randomUUID(),"testuser1@mail.com",
                    null,"testuser2@mail.com", 100, "DEBIT",
                    LocalDateTime.now(),"Debit by transfer",400);
            User user = new User("1", "test", "user 1", "testuser1@mail.com",
                    "test123",true, 500);
            User receiver = new User("2", "test", "user 2", "testuser2@mail.com",
                    "test1234",true, 200);

            when(userRepository.findByUserEmail(debitTransaction.getUserEmail())).thenReturn(user);
            when(userRepository.findByUserEmail(debitTransaction.getReceiverEmail())).thenReturn(receiver);

            assertEquals(TransactionReturnResponse.TRANSACTION_SUCCESS,
                        transactionService.transferWalletAmountImpl(debitTransaction));
        }

        @Test
        @DisplayName("Test For Failed Wallet Amount Transfer - Transaction Amount Is Greater Than User Balance")
        void testTransferWalletSuccessfulDueToInsufficientFunds() {
            Transaction debitTransaction = new Transaction("1",UUID.randomUUID(),"testuser1@mail.com",
                    null,"testuser2@mail.com", 600, "DEBIT",
                    LocalDateTime.now(),"Debit by transfer",500);
            User user = new User("1", "test", "user 1", "testuser1@mail.com",
                    "test123",true, 500);
            User receiver = new User("2", "test", "user 2", "testuser2@mail.com",
                    "test1234",true, 200);

            when(userRepository.findByUserEmail(debitTransaction.getUserEmail())).thenReturn(user);
            when(userRepository.findByUserEmail(debitTransaction.getReceiverEmail())).thenReturn(receiver);

            assertEquals(TransactionReturnResponse.TRANSACTION_FAILED,
                    transactionService.transferWalletAmountImpl(debitTransaction));
        }

        @Test
        @DisplayName("Test For Failed Wallet Amount Transfer - userEmail Is Null")
        void testTransferWalletFailedUserEmailIsNull() {
            Transaction debitTransaction = new Transaction("1",UUID.randomUUID(),null,
                    null,"testuser2@mail.com", 100, "DEBIT",
                    LocalDateTime.now(),"Debit by transfer",0);
            User receiver = new User("2", "test", "user 2", "testuser2@mail.com",
                    "test1234",true, 200);

            when(userRepository.findByUserEmail(debitTransaction.getUserEmail())).thenReturn(null);
            when(userRepository.findByUserEmail(debitTransaction.getReceiverEmail())).thenReturn(receiver);

            assertNull(userRepository.findByUserEmail(debitTransaction.getUserEmail()));

            assertEquals(TransactionReturnResponse.TRANSACTION_FAILED,
                    transactionService.transferWalletAmountImpl(debitTransaction));
        }

        @Test
        @DisplayName("Test For Failed Wallet Amount Transfer - receiverEmail Is Null")
        void testTransferWalletFailedReceiverEmailIsNull() {
            Transaction debitTransaction = new Transaction("1",UUID.randomUUID(),"testuser1@mail.com",
                    null,null, 100, "DEBIT",
                    LocalDateTime.now(),"Debit by transfer",500);
            User user = new User("1", "test", "user 1", "testuser1@mail.com",
                    "test123",true, 500);

            when(userRepository.findByUserEmail(debitTransaction.getUserEmail())).thenReturn(user);
            when(userRepository.findByUserEmail(debitTransaction.getReceiverEmail())).thenReturn(null);

            assertNull(userRepository.findByUserEmail(debitTransaction.getReceiverEmail()));

            assertEquals(TransactionReturnResponse.RECEIVER_NOT_FOUND,
                    transactionService.transferWalletAmountImpl(debitTransaction));
        }

        @Test
        @DisplayName("Test For Failed Wallet Amount Transfer - User Is Not Active")
        void testTransferWalletFailedUserNotActive() {
            Transaction debitTransaction = new Transaction("1",UUID.randomUUID(),"testuser1@mail.com",
                    null,"testuser2@mail.com", 100, "DEBIT",
                    LocalDateTime.now(),"Debit by transfer",500);
            User user = new User("1", "test", "user 1", "testuser1@mail.com",
                    "test123",false, 500);
            User receiver = new User("2", "test", "user 2", "testuser2@mail.com",
                    "test1234",true, 200);

            when(userRepository.findByUserEmail(debitTransaction.getUserEmail())).thenReturn(user);
            when(userRepository.findByUserEmail(debitTransaction.getReceiverEmail())).thenReturn(receiver);

            assertFalse(userRepository.findByUserEmail(debitTransaction.getUserEmail()).isActive());

            assertEquals(TransactionReturnResponse.TRANSACTION_FAILED,
                    transactionService.transferWalletAmountImpl(debitTransaction));
        }

        @Test
        @DisplayName("Test For Failed Wallet Amount Transfer - Invalid Transaction Amount")
        void testTransferWalletFailedInvalidTransactionAmount() {
            Transaction debitTransaction = new Transaction("1",UUID.randomUUID(),"testuser1@mail.com",
                    null,"testuser2@mail.com", 0, "DEBIT",
                    LocalDateTime.now(),"Debit by transfer",500);
            User user = new User("1", "test", "user 1", "testuser1@mail.com",
                    "test123",true, 500);
            User receiver = new User("2", "test", "user 2", "testuser2@mail.com",
                    "test1234",true, 200);

            when(userRepository.findByUserEmail(debitTransaction.getUserEmail())).thenReturn(user);
            when(userRepository.findByUserEmail(debitTransaction.getReceiverEmail())).thenReturn(receiver);

            assertTrue(debitTransaction.getTransactionAmount() <= 0);

            assertEquals(TransactionReturnResponse.TRANSACTION_FAILED,
                    transactionService.transferWalletAmountImpl(debitTransaction));
        }

        @Test
        @DisplayName("Test For Failed Wallet Amount Transfer - Invalid Transaction Type")
        void testTransferWalletFailedInvalidTransactionType() {
            Transaction debitTransaction = new Transaction("1",UUID.randomUUID(),"testuser1@mail.com",
                    null,"testuser2@mail.com", 100, "CREDIT_WITH_TRANSFER",
                    LocalDateTime.now(),"Credit by recharge", 500);
            User user = new User("1", "test", "user 1", "testuser1@mail.com",
                    "test123",true, 500);
            User receiver = new User("2", "test", "user 2", "testuser2@mail.com",
                    "test1234",true, 200);

            when(userRepository.findByUserEmail(debitTransaction.getUserEmail())).thenReturn(user);
            when(userRepository.findByUserEmail(debitTransaction.getReceiverEmail())).thenReturn(receiver);

            assertNotEquals("DEBIT", debitTransaction.getTransactionType());

            assertEquals(TransactionReturnResponse.TRANSACTION_FAILED,
                    transactionService.transferWalletAmountImpl(debitTransaction));
        }
    }

    @Nested
    @DisplayName("Tests For Recharge Wallet")
    class testRechargeWallet {
        @Test
        @DisplayName("Test For Successful Recharge Wallet For Amount Lesser Than 100")
        void testRechargeWalletSuccessfulAmountLesserThanHundred() {
            Transaction transaction = new Transaction("1",UUID.randomUUID(),"testuser1@mail.com",
                    null,null, 10, "CREDIT_BY_RECHARGE",
                    LocalDateTime.now(),"Recharge",60);
            User user = new User("2", "test", "user 1", "testuser1@mail.com",
                    "test123",true, 50.0);

            when(userRepository.findByUserEmail(transaction.getUserEmail())).thenReturn(user);

            assertEquals(TransactionReturnResponse.TRANSACTION_SUCCESS,
                    transactionService.rechargeWalletImpl(transaction));
        }
        @Test
        @DisplayName("Test For Successful Recharge Wallet For Amount Greater Than Or Equal to 100")
        void testRechargeWalletSuccessfulAmountGreaterThanOrEqualToHundred() {
            Transaction transaction = new Transaction("1",UUID.randomUUID(),"testuser1@mail.com",
                    null,null, 100, "CREDIT_BY_RECHARGE",
                    LocalDateTime.now(),"Recharge",60);
            User user = new User("2", "test", "user 1", "testuser1@mail.com",
                    "test123",true, 50.0);

            when(userRepository.findByUserEmail(transaction.getUserEmail())).thenReturn(user);

            assertEquals(TransactionReturnResponse.TRANSACTION_SUCCESS,
                    transactionService.rechargeWalletImpl(transaction));
        }

        @Test
        @DisplayName("Test For Failed Recharge Wallet - userEmail Is Null")
        void testRechargeWalletFailedUserEmailIsNull() {
            Transaction transaction = new Transaction("1",UUID.randomUUID(),null,
                    null,null, 10, "CREDIT_BY_RECHARGE",
                    LocalDateTime.now(),"Recharge",0);

            when(userRepository.findByUserEmail(transaction.getUserEmail())).thenReturn(null);
            assertNull(userRepository.findByUserEmail(transaction.getUserEmail()));

            assertEquals(TransactionReturnResponse.TRANSACTION_FAILED,
                    transactionService.rechargeWalletImpl(transaction));
        }

        @Test
        @DisplayName("Test For Failed Recharge Wallet - User Is Not Active")
        void testRechargeWalletFailedUserNotActive() {
            Transaction transaction = new Transaction("1",UUID.randomUUID(),"testuser1@mail.com",
                    null,null, 10, "CREDIT_BY_RECHARGE",
                    LocalDateTime.now(),"Recharge",0);

            User user = new User("1", "test", "user 1", "testuser1@gmail.com",
                    "test123",false, 0);

            when(userRepository.findByUserEmail(transaction.getUserEmail())).thenReturn(user);

            assertFalse(userRepository.findByUserEmail(transaction.getUserEmail()).isActive());

            assertEquals(TransactionReturnResponse.TRANSACTION_FAILED,
                    transactionService.rechargeWalletImpl(transaction));
        }

        @Test
        @DisplayName("Test For Failed Recharge Wallet - Invalid Transaction Amount")
        void testRechargeWalletFailedInvalidTransactionAmount() {
            Transaction transaction = new Transaction("1",UUID.randomUUID(),"testuser1@mail.com",
                    null,null, 0, "CREDIT_BY_RECHARGE",
                    LocalDateTime.now(),"Recharge",0);

            User user = new User("1", "test", "user 1", "testuser1@gmail.com",
                    "test123",false, 0);

            when(userRepository.findByUserEmail(transaction.getUserEmail())).thenReturn(user);

            assertTrue(transaction.getTransactionAmount() <= 0);

            assertEquals(TransactionReturnResponse.TRANSACTION_FAILED,
                    transactionService.rechargeWalletImpl(transaction));
        }

        @Test
        @DisplayName("Test For Failed Recharge Wallet  - Invalid Transaction Type")
        void testRechargeWalletFailedInvalidTransactionType() {
            Transaction transaction = new Transaction("1",UUID.randomUUID(),"testuser1@mail.com",
                    null,null, 10, "DEBIT",
                    LocalDateTime.now(),"Recharge",0);
            User user = new User("1", "test", "user 1", "testuser1@gmail.com",
                    "test123",true, 0);

            when(userRepository.findByUserEmail(transaction.getUserEmail())).thenReturn(user);

            assertNotEquals("CREDIT_BY_RECHARGE", transaction.getTransactionType());

            assertEquals(TransactionReturnResponse.TRANSACTION_FAILED,
                    transactionService.rechargeWalletImpl(transaction));
        }
    }

    @Nested
    @DisplayName("Tests For View Account Statement")
    class testViewTransactions {
        @Test
        @DisplayName("Test For Successful View Account Statement")
        void testViewTransactionsSuccessful() {
            String userEmail = "testuser1@mail.com";
            List<Transaction> transactions = new ArrayList<>();
            Transaction transaction1 = new Transaction("1",UUID.randomUUID(),"testuser1@mail.com",
                    null,null, 10, "CREDIT_BY_RECHARGE",
                    LocalDateTime.now(),"Recharge", 20);
            Transaction transaction2 = new Transaction("2",UUID.randomUUID(),"testuser1@mail.com",
                    null,null, 10, "CREDIT_BY_RECHARGE",
                    LocalDateTime.now(),"Recharge",30);
            transactions.add(transaction1); transactions.add(transaction2);

            when(transactionRepository.findByUserEmail(userEmail)).thenReturn(transactions);

            List<Transaction> transactionList =
                    transactionService.viewTransactionsImpl(userEmail);

            assertEquals(2, transactionList.size());
            verify(transactionRepository, times(1)).findByUserEmail(userEmail);
        }

        @Test
        @DisplayName("Test For Failed View Account Statement")
        void testViewTransactionsFailed() {
            String userEmail = "testuser1@mail.com";
            List<Transaction> transactions = new ArrayList<>();

            when(transactionRepository.findByUserEmail(userEmail)).thenReturn(transactions);

            List<Transaction> transactionList =
                    transactionService.viewTransactionsImpl(userEmail);

            assertEquals(0, transactionList.size());
            verify(transactionRepository, times(1)).findByUserEmail(userEmail);
        }
    }
}