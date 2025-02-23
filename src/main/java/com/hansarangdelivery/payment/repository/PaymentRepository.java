package com.hansarangdelivery.payment.repository;

import com.hansarangdelivery.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
