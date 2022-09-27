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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    // 리뷰 목록 조회(멤버별)
    @Transactional
    public List<ReviewDto.ReviewResponse> getMemberReviewList() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));

        List<Review> reviews = reviewRepository.findAllByMemberId(member.getId());
        List<ReviewDto.ReviewResponse> reviewList = new ArrayList<>();

        for(Review review : reviews) {
            reviewList.add(ReviewDto.ReviewResponse.of(review));
        }
        return reviewList;
    }

    // 리뷰 목록 조회(상품별)
    @Transactional
    public Page<ReviewDto.ReviewResponse> getProductReviewList(Long productId, Pageable pageable) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new UserException(UserErrorResult.PRODUCT_NOT_FOUND));

        List<Review> reviews = reviewRepository.findAllByProductId(product.getId(),pageable);
        List<ReviewDto.ReviewResponse> reviewList = new ArrayList<>();

        for (Review review : reviews) {
            reviewList.add(ReviewDto.ReviewResponse.of(review));
        }
        return new PageImpl<>(reviewList, pageable,reviews.size());
    }
}
