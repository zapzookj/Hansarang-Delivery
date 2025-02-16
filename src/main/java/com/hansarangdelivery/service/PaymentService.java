package com.hansarangdelivery.service;

import com.hansarangdelivery.entity.Payment;
import com.hansarangdelivery.entity.PaymentMethod;
import com.hansarangdelivery.entity.PaymentStatus;
import com.hansarangdelivery.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public void createMockPayment(Long orderId, int totalPrice) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setTotalPrice(totalPrice);
        payment.setPaymentMethod(PaymentMethod.KAKAO_PAY); // 일단 KAKAO_PAY를 디폴트로 설정
        payment.setPaymentStatus(PaymentStatus.PENDING); // 이것도 일단 디폴트로..
        payment.setTransactionId(UUID.randomUUID().toString()); // 랜덤 UUID 설정

        paymentRepository.save(payment);
    }
}
