package joshi.priyanshu.repository;

import joshi.priyanshu.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByUserEmail(String userEmail);
}
