package com.onulstore.web.dto;

import com.onulstore.domain.enums.ProductStatus;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.wishlist.Wishlist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class WishlistDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class WishlistRequest {

        private Long productId;

        public Wishlist toWishlist(Product product, Member member) {
            return Wishlist.builder()
                    .product(product)
                    .member(member)
                    .build();
        }

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class WishlistResponse {

        private String productName;
        private String content;
        private Integer price;
        private ProductStatus productStatus;

        public static WishlistResponse of(Product product) {
            return WishlistResponse.builder()
                    .productName(product.getProductName())
                    .content(product.getContent())
                    .price(product.getPrice())
                    .productStatus(product.getProductStatus())
                    .build();
        }
    }
}
