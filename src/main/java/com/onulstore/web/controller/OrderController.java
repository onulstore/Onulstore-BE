package com.onulstore.web.controller;

import com.onulstore.service.OrderService;
import com.onulstore.web.dto.OrderDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Api(tags = {"Order-Controller"})
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @ApiOperation(value = "주문 및 결제 내역 조회 / 인증 필요")
    public ResponseEntity<Page<OrderDto.OrderHistory>> getOrderList(Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrderList(pageable));
    }

    @GetMapping("/{orderId}")
    @ApiOperation(value = "해당 주문의 주문 및 결제 내역 조회 / 인증 필요")
    public ResponseEntity<OrderDto.OrderHistory> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping("/entire")
    @ApiOperation(value = "전체 주문 및 결제 내역 조회 / 인증 필요(관리자)")
    public ResponseEntity<Page<OrderDto.OrderHistory>> getAllOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @PostMapping
    @ApiOperation(value = "단일 상품 주문 생성 / 인증 필요")
    public ResponseEntity<String> addCategory(
            @Valid @RequestBody OrderDto.OrderRequest orderRequest) {
        orderService.createOrder(orderRequest);
        return ResponseEntity.ok("상품 주문이 완료되었습니다.");
    }

    @PostMapping("/cartorder")
    @ApiOperation(value = "장바구니 선택 상품 주문 생성 / 인증 필요")
    public ResponseEntity<String> CreateOrderList(
            @RequestBody OrderDto.CartOrderRequest cartOrderRequest) {
        orderService.createSelectedCartOrder(cartOrderRequest);
        return ResponseEntity.ok("상품 주문이 완료되었습니다.");
    }

    @DeleteMapping("/{orderId}")
    @ApiOperation(value = "주문 삭제 / 인증 필요")
    public ResponseEntity<String> orderCancel(@PathVariable Long orderId) {
        orderService.orderCancel(orderId);
        return ResponseEntity.ok("상품 주문이 취소되었습니다.");
    }

    @PutMapping
    @ApiOperation(value = "주문 상태 변경(환불 요청 or 구매 확정) / 인증 필요")
    public ResponseEntity<OrderDto.StatusResponse> updateStatus(
            @Valid @RequestBody OrderDto.StatusRequest request) {
        return ResponseEntity.ok(orderService.updateStatus(request));
    }

    @PutMapping("/{orderId}")
    @ApiOperation(value = "환불 완료 / 인증 필요(관리자)")
    public ResponseEntity<String> orderRefund(@PathVariable Long orderId) {
        orderService.orderRefund(orderId);
        return ResponseEntity.ok("상품 환불이 완료되었습니다.");
    }

    @PutMapping("/update")
    @ApiOperation(value = "해당 주문 회원 정보 변경 / 인증 필요")
    public ResponseEntity<String> orderRefund(
            @RequestBody OrderDto.UpdateOrderRequest updateOrderRequest) {
        orderService.orderModification(updateOrderRequest);
        return ResponseEntity.ok("해당 주문 정보 수정이 완료되었습니다.");
    }

}
