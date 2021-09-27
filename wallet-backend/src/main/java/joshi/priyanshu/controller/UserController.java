package joshi.priyanshu.controller;

import joshi.priyanshu.model.Transaction;
import joshi.priyanshu.returnResponse.UserReturnResponse;
import joshi.priyanshu.model.User;
import joshi.priyanshu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping(path = "/allUsers")
    public List<User> getAllUsers() {
        return userService.getALlUsersImpl();
    }

    @GetMapping(path = "/allUsers/{userEmail}")
    public User getUserDetails(@PathVariable String userEmail) {
        return userService.getUserDetailsImpl(userEmail);
    }

    @PostMapping(path = "/register")
    public UserReturnResponse registerUser(@RequestBody User user){
        String userFirstName = user.getUserFirstName();
        String userLastName = user.getUserLastName();
        String userEmail = user.getUserEmail();
        String userPassword = user.getUserPassword();
        return userService.registerUserImpl(userFirstName, userLastName, userEmail, userPassword);
    }

    @PutMapping(path = "/login")
    public UserReturnResponse loginUser(@RequestBody User user) {
        String userEmail = user.getUserEmail();
        String userPassword = user.getUserPassword();
        return userService.loginUserImpl(userEmail, userPassword);
    }

    @GetMapping(path = "/logout/{userEmail}")
    public UserReturnResponse logoutUser(@PathVariable String userEmail) {
//        String userEmail = user.getUserEmail();
        return userService.logoutUserImpl(userEmail);
    }
}
