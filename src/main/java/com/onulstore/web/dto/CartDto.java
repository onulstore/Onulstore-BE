package com.onulstore.web.dto;

import com.onulstore.domain.cart.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {

  private String memberEmail;
  private Long productId;
  private Integer quantity;
  private String image;

  public static CartDto of(Cart cart){
    return CartDto.builder()
        .memberEmail(cart.getMember().getEmail())
        .productId(cart.getProduct().getId())
        .quantity(cart.getProductCount())
        .image(cart.getProduct().getProductImg())
        .build();
  }

}
