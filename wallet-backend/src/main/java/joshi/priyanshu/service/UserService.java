package joshi.priyanshu.service;

import joshi.priyanshu.model.Transaction;
import joshi.priyanshu.returnResponse.UserReturnResponse;
import joshi.priyanshu.model.User;
import joshi.priyanshu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getALlUsersImpl() {
        List<User> users = userRepository.findAll();
        if(!users.isEmpty()) {
            return users;
        } else {
            return new ArrayList<>();
        }
    }

    public UserReturnResponse registerUserImpl(String userFirstName, String userLastName, String userEmail, String userPassword) {
        User userFromDB = userRepository.findByUserEmail(userEmail);
        if(userFromDB == null){
            User user = setUser(userFirstName, userLastName, userEmail, userPassword);
            userRepository.save(user);
            return UserReturnResponse.USER_SAVED;
        } else {
            return UserReturnResponse.USER_EMAIL_ALREADY_EXISTS;
        }
    }

    private User setUser(String userFirstName, String userLastName, String userEmail, String userPassword) {
        User user = new User();
        user.setUserFirstName(userFirstName);
        user.setUserLastName(userLastName);
        user.setUserEmail(userEmail);
        user.setUserPassword(passwordEncoder.encode(userPassword));
        user.setActive(false);
        user.setBalance(0.0);
        return user;
    }

    public UserReturnResponse loginUserImpl(String userEmail, String userPassword) {
        User userFromDB = userRepository.findByUserEmail(userEmail);
        if(userFromDB == null) {
            return UserReturnResponse.INCORRECT_USEREMAIL_OR_PASSWORD;
        }
        else if(userFromDB.isActive()) {
            return UserReturnResponse.ALREADY_LOGGED_IN;
        }
        else {
            boolean isPasswordMatches = passwordEncoder.matches(userPassword, userFromDB.getUserPassword());
            if (isPasswordMatches) {
                userFromDB.setActive(true);
                userRepository.save(userFromDB);
                return UserReturnResponse.LOGGED_IN;
            } else {
                return UserReturnResponse.INCORRECT_USEREMAIL_OR_PASSWORD;
            }
        }
    }

    public UserReturnResponse logoutUserImpl(String userEmail) {
        User userFromDB = userRepository.findByUserEmail(userEmail);
        if(userFromDB == null) {
            return UserReturnResponse.USER_CANT_BE_LOGGED_OUT;
        }
        else if(!userFromDB.isActive()) {
            return UserReturnResponse.USER_CANT_BE_LOGGED_OUT;
        }
        else {
            userFromDB.setActive(false);
            userRepository.save(userFromDB);
            return UserReturnResponse.LOGOUT_SUCCESS;
        }
    }

    public User getUserDetailsImpl(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        if(user == null) {
            return null;
        }
        else {
            return user;
        }
    }
}
