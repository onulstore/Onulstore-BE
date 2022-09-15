package com.onulstore.web.controller;

import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.service.CartService;
import com.onulstore.web.dto.CartDto;
import io.swagger.annotations.Api;
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
@Api(tags = {"Cart Controller"})
public class CartController {

  private final CartService cartService;
  private final MemberRepository memberRepository;

  @PostMapping("/carts")
  public ResponseEntity addCart(@RequestBody CartDto cartDto) {
    cartService.addCart(cartDto);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/carts/{cartId}")
  public ResponseEntity deleteCart(@PathVariable Long cartId) {
    cartService.deleteCart(cartId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/carts/{cartId}")
  public ResponseEntity<CartDto> shoppingCart(@PathVariable Long cartId) {
    return ResponseEntity.ok(cartService.shoppingCart(cartId));
  }
}