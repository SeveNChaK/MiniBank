package ru.alex.mini_bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alex.mini_bank.service.UserService;
import ru.alex.mini_bank.entity.ExternalUser;
import ru.alex.mini_bank.entity.User;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(
            @RequestParam String uniqueName,
            @RequestParam String password
    ) {
        if (uniqueName == null || password == null) {
            return new ResponseEntity<>("Bad params.", HttpStatus.BAD_REQUEST);
        }

        try {
            final User user = (User) userService.loadUserByUsername(uniqueName);
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return new ResponseEntity<>(new ExternalUser(user), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Wrong credentials.", HttpStatus.BAD_REQUEST);
            }
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(
            @RequestParam String uniqueName,
            @RequestParam String password,
            @RequestParam String confirmPassword
    ) {
        if (uniqueName == null || password == null || confirmPassword == null) {
            return new ResponseEntity<>("Bad params.", HttpStatus.BAD_REQUEST);
        }

        if (!password.equals(confirmPassword)) {
            return new ResponseEntity<>("Passwords not equals.", HttpStatus.BAD_REQUEST);
        }

        String cryptPassword = bCryptPasswordEncoder.encode(password);

        Optional<User> optionalNewUser = userService.saveOrUpdateUser(uniqueName, cryptPassword);
        if (optionalNewUser.isPresent()) {
            return new ResponseEntity<>(new ExternalUser(optionalNewUser.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User already exist.", HttpStatus.BAD_REQUEST);
        }
    }
}
