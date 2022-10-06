package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.Exception;
import com.onulstore.domain.coupon.Coupon;
import com.onulstore.domain.coupon.CouponRepository;
import com.onulstore.domain.enums.CouponStatus;
import com.onulstore.domain.enums.DiscountType;
import com.onulstore.domain.enums.ErrorResult;
import com.onulstore.domain.enums.OrderStatus;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.order.Order;
import com.onulstore.domain.order.OrderProduct;
import com.onulstore.domain.order.OrderProductRepository;
import com.onulstore.domain.order.OrderRepository;
import com.onulstore.domain.payment.Payment;
import com.onulstore.domain.payment.PaymentRepository;
import com.onulstore.web.dto.PaymentDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final OrderProductRepository orderProductRepository;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;

    public PaymentDto.PaymentResponse paying(PaymentDto.PaymentRequest paymentRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        Order order = orderRepository.findById(paymentRequest.getOrderId()).orElseThrow(
            () -> new Exception(ErrorResult.ORDER_NOT_FOUND));
        Coupon coupon = Optional.ofNullable(paymentRequest.getCouponId())
            .map(id -> couponRepository.findById(id).orElseThrow(
                () -> new Exception(ErrorResult.COUPON_NOT_FOUND))).orElse(null);

        if (paymentRepository.existsByOrderId(order.getId())) {
            throw new Exception(ErrorResult.ORDER_ALREADY_PAYED);
        } else if (order.getOrderStatus().equals(OrderStatus.PAYMENT_COMPLETE)) {
            throw new Exception(ErrorResult.ORDER_ALREADY_PAYED);
        } else if (coupon != null && !member.getId().equals(coupon.getMember().getId())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        } else if (coupon != null && coupon.getCouponStatus().equals(CouponStatus.EXPIRED)) {
            throw new Exception(ErrorResult.COUPON_EXPIRED);
        }

        int productPrice = 0;
        int discount = 0;

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrderId(order.getId());
        for (OrderProduct orderProduct : orderProducts) {
            productPrice += orderProduct.getOrderPrice();
        }

        if (coupon != null && coupon.getDiscountType().equals(DiscountType.PERCENT)) {
            discount += productPrice * 0.9;
        }
        discount += paymentRequest.getMileage();
        member.deductPoint(paymentRequest.getMileage());

        int paymentAmount = (productPrice - discount);
        int totalAmount = paymentAmount + paymentRequest.getDeliveryPrice();
        int acquirePoint = (int) (paymentAmount * 0.1);

        Payment payment = Payment.paying(order, coupon, paymentRequest.getMileage(), productPrice,
            discount, paymentRequest.getDeliveryPrice(), totalAmount, acquirePoint,
            paymentRequest.getPaymentMeasure());
        order.paymentSuccess();

        return PaymentDto.PaymentResponse.of(paymentRepository.save(payment));
    }


}
