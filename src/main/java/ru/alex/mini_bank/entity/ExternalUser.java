package ru.alex.mini_bank.entity;

import org.springframework.lang.NonNull;

public class ExternalUser {
    @NonNull
    private final String name;
    @NonNull
    private final String token;
    private final long balance;

    public ExternalUser(@NonNull final User user) {
        this.name = user.getUsername();
        this.token = user.getToken();
        this.balance = user.getBalance();
    }

    @NonNull
    public String getName() {
        return name;
    }

    public long getBalance() {
        return balance;
    }

    @NonNull
    public String getToken() {
        return token;
    }
}
