package joshi.priyanshu.service;

import joshi.priyanshu.model.User;
import joshi.priyanshu.repository.UserRepository;
import joshi.priyanshu.returnResponse.UserReturnResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Autowired
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Nested
    @DisplayName("Tests For Displaying Of All Users' Details")
    class testGetALlUsers {
        @Test
        @DisplayName("Test For Successful Displaying Of All Users' Details")
        void testGetALlUsersSuccessful() {
            List<User> users = new ArrayList<>();
            User user1 = new User("1", "test", "user 1", "testuser1@gmail.com",
                    passwordEncoder.encode("test123"),false, 0);
            User user2 = new User("2", "test",  "user 2","testuser2@gmail.com",
                    passwordEncoder.encode("test1234"), false, 0);
            users.add(user1); users.add(user2);

            when(userRepository.findAll()).thenReturn(users);

            List<User> userList = userService.getALlUsersImpl();

            assertEquals(2, userList.size());
            verify(userRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Test For Failed Displaying Of All Users' Details")
        void testGetALlUsersFailed() {
            List<User> users = new ArrayList<>();

            when(userRepository.findAll()).thenReturn(users);

            List<User> userList = userService.getALlUsersImpl();

            assertEquals(0, userList.size());
            verify(userRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Tests For Displaying Of User's Details")
    class testGetUserDetails{
        @Test
        @DisplayName("Tests For Successful Displaying Of User's Details")
        void testGetUserDetailsSuccess() {
            String userEmail = "testuser1@gmail.com";
            User user = new User("1", "test", "user 1", "testuser1@gmail.com",
                    passwordEncoder.encode("test123"),false, 0);
            when(userRepository.findByUserEmail(userEmail)).thenReturn(user);
            assertEquals(user, userService.getUserDetailsImpl(userEmail));
        }

        @Test
        @DisplayName("Tests For Failed Displaying Of User's Details")
        void testGetUserDetailsFailed() {
            String userEmail = "testuser1@gmail.com";
            when(userRepository.findByUserEmail(userEmail)).thenReturn(null);
            assertEquals(null, userService.getUserDetailsImpl(userEmail));
        }
    }

    @Nested
    @DisplayName("Tests For User Registration")
    class testRegisterUser {
        @Test
        @DisplayName("Test For User Successful Registration")
        void testRegisterUserForSuccessfulRegistration() {
            String userFirstName = "Test";
            String userLastName = "User";
            String userEmail = "test@mail.com";
            String userPassword = passwordEncoder.encode("test123");
            when(userRepository.findByUserEmail(userEmail)).thenReturn(null);
            assertEquals(UserReturnResponse.USER_SAVED,
                    userService.registerUserImpl(userFirstName, userLastName, userEmail, userPassword));
        }

        @Test
        @DisplayName("Test For Failed User Registration - User Is Already Present")
        void testRegisterUserAlreadyPresent() {
            User user = new User("1","test", "user 1","testuser1@gmail.com",
                    passwordEncoder.encode("test123"),false, 0);
            String userFirstName = "test";
            String userLastName = "user 1";
            String userEmail = "testuser1@gmail.com";
            String userPassword = passwordEncoder.encode("test123");
            when(userRepository.findByUserEmail(userEmail)).thenReturn(user);
            assertEquals(UserReturnResponse.USER_EMAIL_ALREADY_EXISTS,
                    userService.registerUserImpl(userFirstName, userLastName, userEmail, userPassword));
        }
    }

    @Nested
    @DisplayName("Tests For Login Of User")
    class testLoginUser {
        @Test
        @DisplayName("Test Of Successful Login Of User")
        void testLoginUserSuccessful() {
            User user = new User("1", "test","user 1", "testuser1@gmail.com",
            passwordEncoder.encode("test123"),false, 0);

            User loginUser = new User("1", "test", "user 1", "testuser1@gmail.com",
                    "test123",false, 0);

            when(userRepository.findByUserEmail(loginUser.getUserEmail())).thenReturn(user);

            assertEquals(UserReturnResponse.LOGGED_IN,
                    userService.loginUserImpl(loginUser.getUserEmail(), loginUser.getUserPassword()));
        }

        @Test
        @DisplayName("Test Of Failed Login - User Not Found")
        void testLoginUserFailedUserNotFound() {
            User loginUser = new User("2", "test", "user 2", "testuser2@gmail.com",
                    "test123",false, 0);

            when(userRepository.findByUserEmail(loginUser.getUserEmail())).thenReturn(null);

            assertEquals(UserReturnResponse.INCORRECT_USEREMAIL_OR_PASSWORD,
                    userService.loginUserImpl(loginUser.getUserEmail(), loginUser.getUserPassword()));
        }

        @Test
        @DisplayName("Test Of Failed Login - User Logged In")
        void testLoginUserFailedUserLoggedIn() {
            User user = new User("1", "test","user 1", "testuser1@gmail.com",
                    passwordEncoder.encode("test123"),true, 0);

            User loginUser = new User("1", "test", "user 1", "testuser1@gmail.com",
                    "test123",false, 0);

            when(userRepository.findByUserEmail(loginUser.getUserEmail())).thenReturn(user);

            assertEquals(UserReturnResponse.ALREADY_LOGGED_IN,
                    userService.loginUserImpl(loginUser.getUserEmail(), loginUser.getUserPassword()));
        }

        @Test
        @DisplayName("Test Of Failed Login - User Password Does Not Match")
        void testLoginUserFailedPasswordDoesNotMatch() {
            User user = new User("1", "test", "user 1", "testuser1@gmail.com",
                    passwordEncoder.encode("test123"),false, 0);

            User loginUser = new User("1", "test", "user 1", "testuser1@gmail.com",
                    "test1234",false, 0);

            when(userRepository.findByUserEmail(loginUser.getUserEmail())).thenReturn(user);

            assertEquals(UserReturnResponse.INCORRECT_USEREMAIL_OR_PASSWORD,
                    userService.loginUserImpl(loginUser.getUserEmail(), loginUser.getUserPassword()));
        }
    }

    @Nested
    @DisplayName("Tests For Logout Of User")
    class testLogoutUser {
        @Test
        @DisplayName("Test For Successful Logout Of User")
        void testLogoutUserSuccessful() {
            String userEmail = "testuser1@gmail.com";
            User user = new User("1", "test", "user 1", "testuser1@gmail.com",
                    passwordEncoder.encode("test123"),true, 0);

            when(userRepository.findByUserEmail(userEmail)).thenReturn(user);

            assertEquals(UserReturnResponse.LOGOUT_SUCCESS, userService.logoutUserImpl(userEmail));
        }

        @Test
        @DisplayName("Test For Failed Logout Of User - User Not Found")
        void testLogoutUserFailedUserNotFound() {
            String userEmail = "testuser1@gmail.com";

            when(userRepository.findByUserEmail(userEmail)).thenReturn(null);

            assertEquals(UserReturnResponse.USER_CANT_BE_LOGGED_OUT, userService.logoutUserImpl(userEmail));
        }

        @Test
        @DisplayName("Test For Failed Logout Of User - User Is Already Logged Out")
        void testLogoutUserFailedIfAlreadyLoggedOut() {
            String userEmail = "testuser1@gmail.com";
            User user = new User("1", "test", "user 1", "testuser1@gmail.com",
                    passwordEncoder.encode("test123"),false, 0);

            when(userRepository.findByUserEmail(userEmail)).thenReturn(user);

            assertEquals(UserReturnResponse.USER_CANT_BE_LOGGED_OUT, userService.logoutUserImpl(userEmail));
        }
    }
}