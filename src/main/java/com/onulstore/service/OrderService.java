package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.CustomException;
import com.onulstore.domain.cart.Cart;
import com.onulstore.domain.cart.CartRepository;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.CustomErrorResult;
import com.onulstore.domain.enums.OrderStatus;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.order.Order;
import com.onulstore.domain.order.OrderProduct;
import com.onulstore.domain.order.OrderProductRepository;
import com.onulstore.domain.order.OrderRepository;
import com.onulstore.domain.payment.Payment;
import com.onulstore.domain.payment.PaymentRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.web.dto.DashboardDto;
import com.onulstore.web.dto.DashboardDto.PaidAndDeliveredOrders;
import com.onulstore.web.dto.OrderDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final OrderProductRepository orderProductRepository;

    /**
     * 단일 상품 주문
     *
     * @param orderRequest
     */
    public void createOrder(OrderDto.OrderRequest orderRequest) {
        confirmLogin();
        Member member = findMember();
        Product product = productRepository.findById(orderRequest.getProductId()).orElseThrow(
                () -> new CustomException(CustomErrorResult.PRODUCT_NOT_FOUND));

        OrderProduct orderProduct = OrderProduct.createOrderProduct(product, orderRequest.getCount());

        Order order = Order.createOrder(
                member,
                orderRequest.getDeliveryMessage(),
                orderRequest.getDeliveryMeasure(),
                orderProduct
        );

        orderRepository.save(order);
    }

    /**
     * 해당 주문 및 주문 결제 정보 조회
     *
     * @param orderId
     * @return 해당 주문 및 주문 결제 정보
     */
    @Transactional(readOnly = true)
    public OrderDto.OrderHistory getOrder(Long orderId) {
        confirmLogin();
        Member member = findMember();
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(CustomErrorResult.ORDER_NOT_FOUND)
        );
        Payment payment = paymentRepository.findByOrderId(order.getId()).orElse(null);

        if (!order.getMember().equals(member)) {
            throw new CustomException(CustomErrorResult.ACCESS_PRIVILEGE);
        }

        OrderDto.OrderHistory orderHistory = new OrderDto.OrderHistory(order);
        List<OrderProduct> orderProductList = order.getOrderProducts();
        for (OrderProduct orderProduct : orderProductList) {
            OrderDto.OrderProduct orderProductDto = new OrderDto.OrderProduct(orderProduct);
            orderHistory.addOrderProduct(orderProductDto);
            if (payment != null) {
                OrderDto.Payment paymentDto = new OrderDto.Payment(payment);
                orderHistory.addPayment(paymentDto);
            }
        }
        return orderHistory;
    }

    /**
     * 본인 주문 내역 및 결제 내역 조회
     *
     * @param pageable
     * @return 주문 내역 및 결제 내역
     */
    @Transactional(readOnly = true)
    public Page<OrderDto.OrderHistory> getOrderList(Pageable pageable) {
        confirmLogin();
        Member member = findMember();

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
     * 전체 주문 조회(관리자)
     *
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<OrderDto.OrderHistory> getAllOrders(Pageable pageable) {
        confirmLogin();
        Member member = findMember();

        authorityCheck(member);

        List<Order> orders = orderRepository.findAll();

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
        return new PageImpl<>(orderHistories, pageable, orders.size());
    }

    /**
     * 장바구니 상품 주문
     *
     * @param cartOrderRequest
     */
    public void createSelectedCartOrder(OrderDto.CartOrderRequest cartOrderRequest) {
        confirmLogin();
        Member member = findMember();
        List<Cart> carts = new ArrayList<>();
        List<OrderProduct> orderProductList = new ArrayList<>();

        for (Long num : cartOrderRequest.getCartList()) {
            carts.add(cartRepository.findById(num).orElseThrow());
        }

        for (Cart cart : carts) {
            orderProductList.add(OrderProduct
                    .createOrderProduct(cart.getProduct(), cart.getProductCount()));
            member.getCarts().remove(cart);
        }

        Order order = Order.createCartOrder(member, cartOrderRequest.getDeliveryMessage(),
                cartOrderRequest.getDeliveryMeasure(), orderProductList);

        orderRepository.save(order);
    }

    /**
     * 주문 취소
     *
     * @param orderId
     */
    public void orderCancel(Long orderId) {
        confirmLogin();
        Member member = findMember();

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(CustomErrorResult.ORDER_NOT_FOUND));

        if (!order.getMember().getId().equals(member.getId())) {
            throw new CustomException(CustomErrorResult.USER_NOT_MATCH);
        }

        order.orderCancel();
    }

    /**
     * 환불 요청 or 구매 확정
     *
     * @param statusRequest
     * @return
     */
    public OrderDto.StatusResponse updateStatus(OrderDto.StatusRequest statusRequest) {
        confirmLogin();
        Member member = findMember();

        if (!(statusRequest.getOrderStatus().equals(OrderStatus.REFUND_REQUEST) ||
                statusRequest.getOrderStatus().equals(OrderStatus.PURCHASE_CONFIRM))) {
            throw new CustomException(CustomErrorResult.ACCESS_PRIVILEGE);
        }

        Order order = orderRepository.findById(statusRequest.getOrderId()).orElseThrow(
                () -> new CustomException(CustomErrorResult.ORDER_NOT_FOUND));

        if (!(order.getMember().getId().equals(member.getId()) || member.getAuthority()
                .equals(Authority.ROLE_ADMIN.getKey()))) {
            throw new CustomException(CustomErrorResult.USER_NOT_MATCH);
        }

        Order updateOrder = order.updateStatus(statusRequest.getOrderStatus());

        Payment payment = paymentRepository.findByOrderId(order.getId()).orElseThrow(
                () -> new CustomException(CustomErrorResult.PAYMENT_NOT_FOUND));

        if (statusRequest.getOrderStatus().equals(OrderStatus.PURCHASE_CONFIRM)) {
            member.acquirePoint(payment.getAcquirePoint());
            List<OrderProduct> orderProducts = orderProductRepository.findAllByOrderId(
                    order.getId());
            for (OrderProduct orderProduct : orderProducts) {
                Product product = productRepository.findById(orderProduct.getProduct().getId())
                        .orElseThrow(() -> new CustomException(CustomErrorResult.PRODUCT_NOT_FOUND));
                product.addPurchaseCount(orderProduct.getCount());
            }
        }

        return OrderDto.StatusResponse.of(updateOrder);
    }

    /**
     * 관리자 환불 완료
     *
     * @param orderId
     */
    public void orderRefund(Long orderId) {
        confirmLogin();
        Member member = findMember();
        authorityCheck(member);

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(CustomErrorResult.ORDER_NOT_FOUND));

        if (!order.getOrderStatus().equals(OrderStatus.REFUND_REQUEST)) {
            throw new CustomException(CustomErrorResult.NOT_REFUND_REQUEST_ORDER);
        }

        order.orderRefund();
    }

    /**
     * 해당 주문의 회원 정보 변경
     *
     * @param updateOrderRequest
     */
    public void orderModification(OrderDto.UpdateOrderRequest updateOrderRequest) {
        confirmLogin();
        findMember();
        Order order = orderRepository.findById(updateOrderRequest.getOrderId()).orElseThrow(
                () -> new CustomException(CustomErrorResult.ORDER_NOT_FOUND));

        order.modificationOrder(updateOrderRequest);
    }

    public Long orderDashBoard(LocalDateTime localDateTime) {
        confirmLogin();
        Member member = findMember();
        authorityCheck(member);

        return orderRepository.countByOrderStatusAndCreatedDateAfter(
                OrderStatus.PURCHASE_CONFIRM,
                localDateTime
        );
    }

    public DashboardDto.TotalSaleAmounts salesAmount(LocalDateTime localDateTime) {
        confirmLogin();
        Member member = findMember();
        authorityCheck(member);

        List<Order> orderList = orderRepository.findAllByOrderStatusAndCreatedDateAfter(
                OrderStatus.PURCHASE_CONFIRM, localDateTime);
        Long totalPrice = 0L;
        Long totalCount = 0L;
        for (Order order : orderList) {
            totalPrice += order.getTotalPrice();
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                totalCount += orderProduct.getCount();
            }
        }
        return DashboardDto.TotalSaleAmounts.builder()
                .totalCount(totalCount)
                .totalPrice(totalPrice)
                .build();
    }

    public DashboardDto.DashboardCategoryResponse salesByCategory(LocalDateTime localDateTime) {
        confirmLogin();
        Member member = findMember();
        authorityCheck(member);

        List<Order> orderList = orderRepository.findAllByOrderStatusAndCreatedDateAfter(
                OrderStatus.PURCHASE_CONFIRM, localDateTime);
        Long fashion = 0L;
        Long living = 0L;
        Long beauty = 0L;
        for (Order order : orderList) {
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                if (orderProduct.getProduct().getCategory().getParent().equals("1")) {
                    fashion += orderProduct.getCount();
                } else if (orderProduct.getProduct().getCategory().getParent().equals("2")) {
                    living += orderProduct.getCount();
                } else {
                    beauty += orderProduct.getCount();
                }
            }
        }
        return DashboardDto.DashboardCategoryResponse.builder()
                .fashionCounts(fashion)
                .livingCounts(living)
                .beautyCounts(beauty)
                .build();
    }

    public PaidAndDeliveredOrders paidAndDeliveredOrders(LocalDateTime localDateTime) {
        confirmLogin();
        Member member = findMember();
        authorityCheck(member);

        Long paidOrders = orderRepository.countByOrderStatusAndCreatedDateAfter(
                OrderStatus.PAYMENT_COMPLETE,
                localDateTime
        );
        Long deliveredOrders = orderRepository.countByOrderStatusAndCreatedDateAfter(
                OrderStatus.PURCHASE_CONFIRM,
                localDateTime
        );
        Long entireOrders = orderRepository.countByCreatedDateAfter(localDateTime);
        return PaidAndDeliveredOrders.builder()
                .paidOrders(paidOrders)
                .deliveredOrders(deliveredOrders)
                .entireOrders(entireOrders)
                .build();
    }

    public DashboardDto.DailyStatistic dailyOrderStatistic(LocalDateTime localDateTime, OrderStatus orderStatus) {
        confirmLogin();
        Member member = findMember();
        authorityCheck(member);

        List<Long> dailyOrderStatistic = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dailyOrderStatistic.add(
                    orderRepository.countByOrderStatusAndCreatedDateBetween(orderStatus,
                            localDateTime.plusDays(i), localDateTime.plusDays(i + 1)));
        }
        return DashboardDto.DailyStatistic.builder()
                .statistic(dailyOrderStatistic)
                .build();
    }

    public List<OrderDto.OrderResponse> recentOrders() {
        return orderRepository.findTop10ByOrderByCreatedDateDesc()
                .stream()
                .map(OrderDto.OrderResponse::of)
                .collect(Collectors.toList());
    }

    private static void confirmLogin() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
    }

    private Member findMember() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
    }

    private static void authorityCheck(Member member) {
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new CustomException(CustomErrorResult.ACCESS_PRIVILEGE);
        }
    }

}