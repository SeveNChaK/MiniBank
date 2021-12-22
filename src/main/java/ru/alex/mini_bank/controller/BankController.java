package ru.alex.mini_bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alex.mini_bank.service.UserService;
import ru.alex.mini_bank.entity.ExternalUser;
import ru.alex.mini_bank.entity.User;
import ru.alex.mini_bank.service.PaymentService;

import java.security.Principal;

@RestController
public class BankController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserService userService;

    @GetMapping("/status")
    public String status() {
        return "I am OK!";
    }

    @GetMapping("/api/main")
    public ResponseEntity<?> mainPage(@Nullable Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>("Need login.", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>("Access.", HttpStatus.OK);
    }

    @GetMapping("/api/transactions")
    public ResponseEntity<?> allTransaction(@Nullable Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>("Need login.", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(paymentService.getAllPayments(principal.getName()), HttpStatus.OK);
    }

    @PostMapping("/api/addMoney")
    public ResponseEntity<?> addMoney(
            @Nullable Principal principal,
            @RequestParam long amount
    ) {
        if (principal == null) {
            return new ResponseEntity<>("Need login.", HttpStatus.UNAUTHORIZED);
        }

        try {
            User newUser = paymentService.addMoney(principal.getName(), amount);
            return new ResponseEntity<>(new ExternalUser(newUser), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/getMoney")
    public ResponseEntity<?> getMoney(
            @Nullable Principal principal,
            @RequestParam long amount
    ) {
        if (principal == null) {
            return new ResponseEntity<>("Need login.", HttpStatus.UNAUTHORIZED);
        }

        try {
            User newUser = paymentService.getMoney(principal.getName(), amount);
            return new ResponseEntity<>(new ExternalUser(newUser), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>("Low money.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/send")
    public ResponseEntity<?> sendMoney(
            @Nullable Principal principal,
            @RequestParam String targetName,
            @RequestParam long amount,
            @RequestParam(required = false) String note
    ) {
        if (principal == null) {
            return new ResponseEntity<>("Need login.", HttpStatus.UNAUTHORIZED);
        }

        try {
            User newUser = paymentService.sendMoney(principal.getName(), targetName, amount, note);
            return new ResponseEntity<>(new ExternalUser(newUser), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>("Low money.", HttpStatus.BAD_REQUEST);
        }
    }
}
