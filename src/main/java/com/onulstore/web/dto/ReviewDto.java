package com.onulstore.web.dto;

import com.onulstore.domain.member.Member;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.review.Review;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReviewRequest {

        private Long productId;
        private String content;
        private float rate;

        public Review toReview(Member member, Product product) {
            return Review.builder()
                .member(member)
                .product(product)
                .title("title")
                .content(content)
                .rate(rate)
                .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReviewResponse {

        private Long memberId;
        private Long reviewId;
        private Long productId;
        private String title;
        private String content;
        private float rate;
        private LocalDateTime createDate;

        public static ReviewResponse of(Review review) {
            return ReviewResponse.builder()
                .memberId(review.getMember().getId())
                .reviewId(review.getId())
                .productId(review.getProduct().getId())
                .title(review.getTitle())
                .content(review.getContent())
                .rate(review.getRate())
                .createDate(review.getCreatedDate())
                .build();
        }
    }
}
