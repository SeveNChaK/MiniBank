package ru.alex.mini_bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.alex.mini_bank.entity.Payment;
import ru.alex.mini_bank.entity.User;
import ru.alex.mini_bank.repository.PaymentRepository;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserService userService;

    public List<Payment> getAllPayments(@NonNull String uniqueName) {
        return paymentRepository.findAllBySourceNameAndTargetName(uniqueName, uniqueName);
    }

    public User addMoney(@NonNull String uniqueName, long amount) throws UsernameNotFoundException {
        User user = (User) userService.loadUserByUsername(uniqueName);
        long absAmount = Math.abs(amount);
        user.setBalance(
                user.getBalance() + absAmount
        );
        User newUser = userService.updateUser(user);

        long timestamp = System.currentTimeMillis();
        Payment payment = new Payment();
        payment.setSourceName(user.getUsername());
        payment.setTargetName(user.getUsername());
        payment.setAmount(absAmount);
        payment.setTimestamp(timestamp);
        paymentRepository.save(payment);

        return newUser;
    }

    public User getMoney(@NonNull String uniqueName, long amount) throws UsernameNotFoundException {
        User user = (User) userService.loadUserByUsername(uniqueName);
        long absAmount = Math.abs(amount);
        if (user.getBalance() - absAmount < 0) {
            throw new IllegalStateException("Low money.");
        }

        user.setBalance(
                user.getBalance() - absAmount
        );
        User newUser = userService.updateUser(user);

        long timestamp = System.currentTimeMillis();
        Payment payment = new Payment();
        payment.setSourceName(user.getUsername());
        payment.setTargetName(user.getUsername());
        payment.setAmount(-absAmount);
        payment.setTimestamp(timestamp);
        paymentRepository.save(payment);

        return newUser;
    }

    public User sendMoney(@NonNull String sourceName, @NonNull String targetName, long amount, @NonNull String note) throws UsernameNotFoundException {
        User sourceUser = (User) userService.loadUserByUsername(sourceName);
        User targetUser = (User) userService.loadUserByUsername(targetName);
        long absAmount = Math.abs(amount);
        if (sourceUser.getBalance() - absAmount < 0) {
            throw new IllegalStateException("Low money.");
        }

        sourceUser.setBalance(
                sourceUser.getBalance() - absAmount
        );
        User newUser = userService.updateUser(sourceUser);

        targetUser.setBalance(
                targetUser.getBalance() + absAmount
        );
        userService.updateUser(targetUser);

        long timestamp = System.currentTimeMillis();
        Payment payment = new Payment();
        payment.setSourceName(sourceName);
        payment.setTargetName(targetName);
        payment.setAmount(absAmount);
        payment.setTimestamp(timestamp);
        payment.setNote(note);
        paymentRepository.save(payment);

        return newUser;
    }
}
