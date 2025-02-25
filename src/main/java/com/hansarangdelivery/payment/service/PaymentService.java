package com.hansarangdelivery.payment.service;

import com.hansarangdelivery.payment.model.Payment;
import com.hansarangdelivery.payment.model.PaymentMethod;
import com.hansarangdelivery.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public boolean createMockPayment(UUID orderId, int totalPrice) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setTotalPrice(totalPrice);
        payment.setPaymentMethod(PaymentMethod.CARD); // 결제 방법은 카드 결제를 디폴트로 설정
        payment.setSuccess(true); // 결제 성공 여부는 성공으로 디폴트 설정
        payment.setTransactionId(UUID.randomUUID().toString()); // 랜덤 UUID 설정

        paymentRepository.save(payment);
        return true; // 결제 성공 여부 반환
    }
}
