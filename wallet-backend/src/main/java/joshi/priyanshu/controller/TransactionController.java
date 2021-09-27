package joshi.priyanshu.controller;

import joshi.priyanshu.returnResponse.TransactionReturnResponse;
import joshi.priyanshu.model.Transaction;
import joshi.priyanshu.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping(path = "/transfer")
    public TransactionReturnResponse transferWalletAmount(@RequestBody Transaction transaction) {
        return transactionService.transferWalletAmountImpl(transaction);
    }

    @PostMapping(path = "/recharge")
    public TransactionReturnResponse rechargeWallet(@RequestBody Transaction transaction) {
        return transactionService.rechargeWalletImpl(transaction);
    }

    @GetMapping(path = "/viewTransaction/{userEmail}")
    public List<Transaction> viewTransactions(@PathVariable String userEmail) {
        return transactionService.viewTransactionsImpl(userEmail);
    }
}
