package ru.alex.mini_bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import ru.alex.mini_bank.entity.Payment;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllBySourceNameAndTargetName(@NonNull String sourceName, @NonNull String targetName);
}
