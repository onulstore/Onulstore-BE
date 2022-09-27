package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.domain.enums.UserErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.domain.review.Review;
import com.onulstore.domain.review.ReviewRepository;
import com.onulstore.exception.UserException;
import com.onulstore.web.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // 리뷰 등록
    @Transactional
    public ReviewDto.ReviewResponse insertReview(ReviewDto.ReviewRequest request) {
      Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
      Product product = productRepository.findById(request.getProductId()).orElseThrow(
            () -> new UserException(UserErrorResult.PRODUCT_NOT_FOUND));

      Review review = request.toReview(member,product);

      return ReviewDto.ReviewResponse.of(reviewRepository.save(review));
    }

    // 리뷰 수정
    @Transactional
    public ReviewDto.ReviewResponse updateReview(Long reviewId, ReviewDto.ReviewRequest request) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        Review review = reviewRepository.findById(reviewId).orElseThrow();

        if (!member.getId().equals(review.getMember().getId())){
            throw new UserException(UserErrorResult.USER_NOT_MATCH);
        }
        review.setTitle(request.getTitle());
        review.setContent(request.getContent());
        review.setRate(request.getRate());

        return ReviewDto.ReviewResponse.of(reviewRepository.save(review));
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId){
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));

        Review review = reviewRepository.findById(reviewId).orElseThrow();

        if (!member.getId().equals(review.getMember().getId())){
            throw new UserException(UserErrorResult.USER_NOT_MATCH);
        }
        reviewRepository.delete(review);
    }

    // 리뷰 상세 조회
    @Transactional
    public ReviewDto.ReviewResponse getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        return ReviewDto.ReviewResponse.of(review);
    }
}
