package com.onulstore.web.dto;

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
  private Long cartId;

}
