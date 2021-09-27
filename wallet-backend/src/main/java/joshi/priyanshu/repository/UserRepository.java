package joshi.priyanshu.repository;

import joshi.priyanshu.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUserEmail(String userEmail);
}
