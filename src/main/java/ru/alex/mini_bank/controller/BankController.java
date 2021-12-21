package ru.alex.mini_bank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankController {

    @GetMapping("/status")
    public String status() {
        return "I am OK!";
    }
}
