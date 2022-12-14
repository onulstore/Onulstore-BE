package com.onulstore.web.dto;

import com.onulstore.domain.cart.Cart;
import com.onulstore.web.dto.ProductImageDto.ProductImageMaker;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CartDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CartRequest {

        private Long productId;
        private Integer quantity;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CartResponse {

        private String memberEmail;
        private Long productId;
        private Long cartId;
        private Integer quantity;
        private List<ProductImageMaker> images;

        public static CartResponse of(Cart cart) {
            return CartResponse.builder()
                .memberEmail(cart.getMember().getEmail())
                .productId(cart.getProduct().getId())
                .cartId(cart.getId())
                .quantity(cart.getProductCount())
                .images(ProductImageMaker.of(cart.getProduct().getProductImages()))
                .build();
        }
    }

}
