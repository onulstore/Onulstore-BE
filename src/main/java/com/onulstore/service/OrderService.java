package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.Exception;
import com.onulstore.domain.cart.Cart;
import com.onulstore.domain.cart.CartRepository;
import com.onulstore.domain.category.Category;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.ErrorResult;
import com.onulstore.domain.enums.OrderStatus;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.order.Order;
import com.onulstore.domain.order.OrderProduct;
import com.onulstore.domain.order.OrderRepository;
import com.onulstore.domain.payment.Payment;
import com.onulstore.domain.payment.PaymentRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.web.dto.OrderDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    /**
     * 단일 상품 주문
     * @param orderRequest
     */
    public void createOrder(OrderDto.OrderRequest orderRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(orderRequest.getProductId()).orElseThrow(
            () -> new Exception(ErrorResult.PRODUCT_NOT_FOUND));

        OrderProduct orderProduct =
            OrderProduct.createOrderProduct(product, orderRequest.getCount());

        Order order = Order.createOrder(member, orderRequest.getDeliveryMessage(),
            orderRequest.getDeliveryMeasure(), orderProduct);

        orderRepository.save(order);
    }

    /**
     * 본인 주문 내역 및 결제 내역 조회
     * @param pageable
     * @return 주문 내역 및 결제 내역
     */
    @Transactional(readOnly = true)
    public Page<OrderDto.OrderHistory> getOrderList(Pageable pageable) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));

        List<Order> orders = orderRepository.findOrders(member.getEmail(), pageable);
        Long totalCount = orderRepository.countOrder(member.getEmail());

        List<Payment> payments = new ArrayList<>();
        for (long orderId = 1L; orderId < orders.size(); orderId++) {
            payments = paymentRepository.findPaymentsByOrderId(
                orders.get(Math.toIntExact(orderId)).getId(), pageable);
        }

        List<OrderDto.OrderHistory> orderHistories = new ArrayList<>();

        for (Order order : orders) {
            OrderDto.OrderHistory orderHistory = new OrderDto.OrderHistory(order);
            List<OrderProduct> orderProductList = order.getOrderProducts();
            for (OrderProduct orderProduct : orderProductList) {
                OrderDto.OrderProduct orderProductDto = new OrderDto.OrderProduct(orderProduct);
                orderHistory.addOrderProduct(orderProductDto);
                for (Payment payment : payments) {
                    OrderDto.Payment paymentDto = new OrderDto.Payment(payment);
                    orderHistory.addPayment(paymentDto);
                }
            }
            orderHistories.add(orderHistory);
        }
        return new PageImpl<>(orderHistories, pageable, totalCount);
    }

    /**
     * 장바구니 상품 주문
     * @param cartOrderRequest
     */
    public void createSelectedCartOrder(OrderDto.CartOrderRequest cartOrderRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        List<Cart> carts = new ArrayList<>();
        List<OrderProduct> orderProductList = new ArrayList<>();

        for (Long num : cartOrderRequest.getCartList()) {
            carts.add(cartRepository.findById(num).orElseThrow());
        }

        for (Cart cart : carts) {
            orderProductList.add(OrderProduct
                .createOrderProduct(cart.getProduct(), cart.getProductCount()));
        }

        Order order = Order.createCartOrder(member, cartOrderRequest.getDeliveryMessage(),
            cartOrderRequest.getDeliveryMeasure(), orderProductList);

        orderRepository.save(order);
    }

    /**
     * 주문 취소
     * @param orderId
     */
    public void orderCancel(Long orderId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));

        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new Exception(ErrorResult.ORDER_NOT_FOUND));

        if (!order.getMember().getId().equals(member.getId())) {
            throw new Exception(ErrorResult.USER_NOT_MATCH);
        }

        order.orderCancel();
    }

    /**
     * 환불 요청 or 구매 확정
     * @param statusRequest
     * @return
     */
    public OrderDto.StatusResponse updateStatus(OrderDto.StatusRequest statusRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));

        if (!(statusRequest.getOrderStatus().equals(OrderStatus.REFUND_REQUEST) ||
            statusRequest.getOrderStatus().equals(OrderStatus.PURCHASE_CONFIRM))) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Order order = orderRepository.findById(statusRequest.getOrderId()).orElseThrow(
            () -> new Exception(ErrorResult.ORDER_NOT_FOUND));

        if (!(order.getMember().getId().equals(member.getId()) || member.getAuthority()
            .equals(Authority.ROLE_ADMIN.getKey()))) {
            throw new Exception(ErrorResult.USER_NOT_MATCH);
        }

        Order updateOrder = order.updateStatus(statusRequest.getOrderStatus());

        Payment payment = paymentRepository.findByOrderId(order.getId()).orElseThrow(
            () -> new Exception(ErrorResult.PAYMENT_NOT_FOUND));

        if (statusRequest.getOrderStatus().equals(OrderStatus.PURCHASE_CONFIRM)) {
            member.acquirePoint(payment.getAcquirePoint());
        }

        return OrderDto.StatusResponse.of(updateOrder);
    }

    public Integer orderDashBoard(LocalDateTime localDateTime) {
        List<Order> orderList = orderRepository.findAllByOrderStatusAndCreatedDateAfter(
            OrderStatus.PURCHASE_CONFIRM, localDateTime);
        return orderList.size();
    }

    public List<Integer> salesAmount(LocalDateTime localDateTime) {
        List<Order> orderList = orderRepository.findAllByOrderStatusAndCreatedDateAfter(
            OrderStatus.PURCHASE_CONFIRM, localDateTime);
        Integer totalPrice = 0;
        Integer totalCount = 0;
        for (Order order : orderList) {
            totalPrice += order.getTotalPrice();
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                totalCount += orderProduct.getCount();
            }
        }
        List<Integer> salesAmount = new ArrayList<>();
        salesAmount.add(totalPrice);
        salesAmount.add(totalCount);
        return salesAmount;
    }

    public List<Integer> salesByCategory(LocalDateTime localDateTime) {
        List<Order> orderList = orderRepository.findAllByOrderStatusAndCreatedDateAfter(
            OrderStatus.PURCHASE_CONFIRM, localDateTime);
        Integer fashion = 0;
        Integer living = 0;
        Integer beauty = 0;
        for(Order order : orderList){
            for(OrderProduct orderProduct : order.getOrderProducts()){
                if(orderProduct.getProduct().getCategory().getParent().equals("1")){
                    fashion += orderProduct.getCount();
                }
                else if(orderProduct.getProduct().getCategory().getParent().equals("2")){
                    living += orderProduct.getCount();
                }
                else{
                    beauty += orderProduct.getCount();
                }
            }
        }
        List<Integer> countByCategory = Arrays.asList(fashion, living, beauty);
        return countByCategory;
    }

}

    /**
     * 관리자 환불 완료
     * @param orderId
     */
    public void orderRefund(Long orderId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));

        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new Exception(ErrorResult.ORDER_NOT_FOUND));

        if (!order.getOrderStatus().equals(OrderStatus.REFUND_REQUEST)) {
            throw new Exception(ErrorResult.NOT_REFUND_REQUEST_ORDER);
        }

        order.orderRefund();
    }

    /**
     * 해당 주문의 회원 정보 변경
     * @param updateOrderRequest
     */
    public void orderModification(OrderDto.UpdateOrderRequest updateOrderRequest) {
        memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        Order order = orderRepository.findById(updateOrderRequest.getOrderId()).orElseThrow(
            () -> new Exception(ErrorResult.ORDER_NOT_FOUND));

        order.modificationOrder(updateOrderRequest);
    }

}