package com.onulstore.web.controller;

import com.onulstore.domain.member.MemberRepository;
import com.onulstore.service.CartService;
import com.onulstore.web.dto.CartDto;
import com.onulstore.web.dto.CartDto.CartResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Cart-Controller"})
public class CartController {

    private final CartService cartService;

    @ApiOperation(value = "장바구니 등록 / 인증 필요")
    @PostMapping("/carts")
    public ResponseEntity addCart(@RequestBody CartDto.CartRequest cartRequest) {
        cartService.addCart(cartRequest);
        return ResponseEntity.ok("장바구니에 상품을 담았습니다.");
    }

    @ApiOperation(value = "장바구니 제거 / 인증 필요")
    @DeleteMapping("/carts/{cartId}")
    public ResponseEntity deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.ok("장바구니를 삭제하였습니다.");
    }

    @ApiOperation(value = "장바구니 조회 / 인증 필요")
    @GetMapping("/carts")
    public ResponseEntity<List<CartDto.CartResponse>> getCartList() {
        return ResponseEntity.ok(cartService.getCartList());
    }

    @ApiOperation(value = "장바구니 수량 증가버튼 / 인증 필요")
    @PostMapping("/carts/{cartId}/plus")
    public ResponseEntity<CartDto.CartResponse> plusQuantity(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.plusOne(cartId));
    }

    @ApiOperation(value = "장바구니 수량 감소버튼 / 인증 필요")
    @PostMapping("/carts/{cartId}/minus")
    public ResponseEntity<CartDto.CartResponse> minusQuantity(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.minusOne(cartId));
    }

}
