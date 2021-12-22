package ru.alex.mini_bank.entity;

import org.springframework.lang.NonNull;

public class ExternalUser {
    @NonNull
    private final String name;
    private final long balance;

    public ExternalUser(@NonNull final User user) {
        this.name = user.getUsername();
        this.balance = user.getBalance();
    }

    @NonNull
    public String getName() {
        return name;
    }

    public long getBalance() {
        return balance;
    }
}
