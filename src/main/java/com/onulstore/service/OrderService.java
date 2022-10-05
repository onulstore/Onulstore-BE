package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.Exception;
import com.onulstore.domain.cart.Cart;
import com.onulstore.domain.cart.CartRepository;
import com.onulstore.domain.enums.ErrorResult;
import com.onulstore.domain.enums.OrderStatus;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.order.Order;
import com.onulstore.domain.order.OrderProduct;
import com.onulstore.domain.order.OrderRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.web.dto.OrderDto;
import java.util.ArrayList;
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

    /**
     * 단일 상품 주문
     * @param orderRequest
     */
    public void createOrder(OrderDto.OrderRequest orderRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(orderRequest.getProductId())
            .orElseThrow(() -> new Exception(ErrorResult.PRODUCT_NOT_FOUND));

        OrderProduct orderProduct =
            OrderProduct.createOrderProduct(product, orderRequest.getCount());

        Order order = Order.createOrder(member, orderRequest.getDeliveryMessage(), orderProduct);

        orderRepository.save(order);
    }

    /**
     * 본인 주문 내역
     * @param pageable
     * @return 주문 내역
     */
    @Transactional(readOnly = true)
    public Page<OrderDto.OrderHistory> getOrderList(Pageable pageable) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));

        List<Order> orders = orderRepository.findOrders(member.getEmail(), pageable);
        Long totalCount = orderRepository.countOrder(member.getEmail());

        List<OrderDto.OrderHistory> orderHistories = new ArrayList<>();

        for (Order order : orders) {
            OrderDto.OrderHistory orderHistory = new OrderDto.OrderHistory(order);
            List<OrderProduct> orderProductList = order.getOrderProducts();
            for (OrderProduct orderProduct : orderProductList) {
                OrderDto.OrderProduct orderProductDto = new OrderDto.OrderProduct(orderProduct);
                orderHistory.addOrderProduct(orderProductDto);
            }
            orderHistories.add(orderHistory);
        }
        return new PageImpl<>(orderHistories, pageable, totalCount);
    }

    public Long createSelectedCartOrder(List<Long> cartList) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        List<Cart> carts = new ArrayList<>();
        List<OrderProduct> orderProductList = new ArrayList<>();

        for (Long num : cartList) {
            carts.add(cartRepository.findById(num).orElseThrow());
        }

        for (Cart cart : carts) {
            orderProductList.add(OrderProduct
                .createOrderProduct(cart.getProduct(), cart.getProductCount()));
        }

        Order order = Order.createCartOrder(member, orderProductList);

        orderRepository.save(order);
        return order.getId();
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

        if (!(statusRequest.getOrderStatus().equals(OrderStatus.REFUND_REQUEST.getKey()) ||
            statusRequest.getOrderStatus().equals(OrderStatus.PURCHASE_CONFIRM.getKey()))) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Order order = orderRepository.findById(statusRequest.getOrderId()).orElseThrow(
            () -> new Exception(ErrorResult.ORDER_NOT_FOUND));

        if (!order.getMember().getId().equals(member.getId())) {
            throw new Exception(ErrorResult.USER_NOT_MATCH);
        }

        Order updateOrder = order.updateStatus(statusRequest.getOrderStatus());
        return OrderDto.StatusResponse.of(updateOrder);
    }

}
