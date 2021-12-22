package ru.alex.mini_bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alex.mini_bank.service.UserService;
import ru.alex.mini_bank.entity.ExternalUser;
import ru.alex.mini_bank.entity.User;

import java.security.Principal;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(
            @RequestParam String uniqueName,
            @RequestParam String password
    ) {
        if (uniqueName == null || password == null) {
            return new ResponseEntity<>("Bad params.", HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userService.signIn(uniqueName, password);
        if (optionalUser.isPresent()) {
            return new ResponseEntity<>(new ExternalUser(optionalUser.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Wrong credentials.", HttpStatus.BAD_REQUEST);
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

        Optional<User> optionalNewUser = userService.signUp(uniqueName, password);
        if (optionalNewUser.isPresent()) {
            return new ResponseEntity<>(new ExternalUser(optionalNewUser.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User already exist.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/signOut")
    public ResponseEntity<?> signOut(@Nullable Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>("Signed out.", HttpStatus.OK);
        }

        boolean signedOut = userService.signOut(principal.getName());
        if (signedOut) {
            return new ResponseEntity<>("Signed out", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
