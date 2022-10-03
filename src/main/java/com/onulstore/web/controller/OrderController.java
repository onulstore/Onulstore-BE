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
    @ApiOperation(value = "주문 조회")
    public ResponseEntity<Page<OrderDto.OrderHistory>> getOrderList(Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrderList(pageable));
    }

    @PostMapping
    @ApiOperation(value = "단일 상품 주문 생성")
    public ResponseEntity<String> addCategory(@Valid @RequestBody OrderDto.OrderRequest orderRequest) {
        orderService.createOrder(orderRequest);
        return ResponseEntity.ok("상품 주문이 완료되었습니다.");
    }

    @PostMapping("/cartorder")
    @ApiOperation(value = "장바구니 선택 상품 주문 생성")
    public ResponseEntity<String> CreateOrderList(@RequestBody List<Long> cartList) {
        orderService.createSelectedCartOrder(cartList);
        return ResponseEntity.ok("상품 주문이 완료되었습니다.");
    }

    @DeleteMapping("/{orderId}")
    @ApiOperation(value = "주문 삭제")
    public ResponseEntity<String> orderCancel(@PathVariable Long orderId) {
        orderService.orderCancel(orderId);
        return ResponseEntity.ok("상품 주문이 취소되었습니다.");
    }

    @PutMapping
    @ApiOperation(value = "주문 상태 변경")
    public ResponseEntity<OrderDto.StatusResponse> updateStatus(@Valid @RequestBody OrderDto.StatusRequest request) {
        return ResponseEntity.ok(orderService.updateStatus(request));
    }

}
