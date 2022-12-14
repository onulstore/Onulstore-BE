package com.onulstore.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.CustomException;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.CustomErrorResult;
import com.onulstore.domain.enums.OrderStatus;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.order.Order;
import com.onulstore.domain.order.OrderRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.domain.review.Review;
import com.onulstore.domain.review.ReviewImage;
import com.onulstore.domain.review.ReviewImageRepository;
import com.onulstore.domain.review.ReviewRepository;
import com.onulstore.web.dto.ReviewDto;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReviewService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client s3Client;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewImageRepository reviewImageRepository;

    // 리뷰 등록
    @Transactional
    public ReviewDto.ReviewResponse insertReview(ReviewDto.ReviewRequest request, Long orderId) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
            () -> new CustomException(CustomErrorResult.PRODUCT_NOT_FOUND));
        Order order = orderRepository.findByIdAndMember(orderId, member).orElseThrow(
            () -> new CustomException(CustomErrorResult.ORDER_NOT_FOUND));

        if (!order.getOrderStatus().equals(OrderStatus.PURCHASE_CONFIRM)) {
            throw new CustomException(CustomErrorResult.NOT_PURCHASE_CONFIRM_ORDER);
        }

        Long count = reviewRepository.countByProduct(product);
        Review review = request.toReview(member, product);

        if (product.getRating() == 0) {
            product.setRating(request.getRate());
        } else {
            product.setRating((product.getRating() * count + request.getRate()) / (count + 1));
        }

        return ReviewDto.ReviewResponse.of(reviewRepository.save(review));
    }

    // 리뷰 수정
    @Transactional
    public ReviewDto.ReviewResponse updateReview(Long reviewId, ReviewDto.ReviewRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(CustomErrorResult.REVIEW_NOT_FOUND));

        if (!member.getId().equals(review.getMember().getId())) {
            throw new CustomException(CustomErrorResult.USER_NOT_MATCH);
        }
        review.setContent(request.getContent());
        review.setRate(request.getRate());

        return ReviewDto.ReviewResponse.of(reviewRepository.save(review));
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(CustomErrorResult.REVIEW_NOT_FOUND));

        if (!member.getId().equals(review.getMember().getId())) {
            throw new CustomException(CustomErrorResult.USER_NOT_MATCH);
        }
        reviewRepository.delete(review);
    }

    // 리뷰 상세 조회
    @Transactional
    public ReviewDto.ReviewResponse getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(CustomErrorResult.REVIEW_NOT_FOUND));
        return ReviewDto.ReviewResponse.of(review);
    }

    // 리뷰 목록 조회(멤버별)
    @Transactional
    public List<ReviewDto.ReviewResponse> getMemberReviewList() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));

        List<Review> reviews = reviewRepository.findAllByMemberId(member.getId());
        List<ReviewDto.ReviewResponse> reviewList = new ArrayList<>();

        for (Review review : reviews) {
            reviewList.add(ReviewDto.ReviewResponse.of(review));
        }
        return reviewList;
    }

    // 리뷰 목록 조회(상품별)
    @Transactional
    public Page<ReviewDto.ReviewResponse> getProductReviewList(Long productId, Pageable pageable) {
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new CustomException(CustomErrorResult.PRODUCT_NOT_FOUND));

        List<Review> reviews = reviewRepository.findAllByProductId(product.getId(), pageable);
        List<ReviewDto.ReviewResponse> reviewList = new ArrayList<>();

        for (Review review : reviews) {
            reviewList.add(ReviewDto.ReviewResponse.of(review));
        }
        return new PageImpl<>(reviewList, pageable, reviews.size());
    }

    // 리뷰 이미지 등록
    @Transactional
    public void uploadImages(List<MultipartFile> multipartFiles, Long reviewId) throws IOException {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        Review review = reviewRepository.findByMemberAndId(member, reviewId).orElseThrow(
            () -> new CustomException(CustomErrorResult.REVIEW_NOT_FOUND));

        for (MultipartFile multipartFile : multipartFiles) {
            ReviewImage reviewImage = new ReviewImage();
            InputStream inputStream = multipartFile.getInputStream();

            String originFileName = multipartFile.getOriginalFilename();
            String s3FileName = UUID.randomUUID() + "-" + originFileName;
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentType(multipartFile.getContentType());
            s3Client.putObject(bucket, s3FileName, inputStream, objMeta);

            reviewImage.setImageName(s3FileName);
            reviewImage.setReview(review);
            reviewImageRepository.save(reviewImage);
            review.getReviewImages().add(reviewImage);
        }
    }

    @Transactional
    public Long reviewDashBoard(LocalDateTime localDateTime) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new CustomException(CustomErrorResult.ACCESS_PRIVILEGE);
        }

        Long reviews = reviewRepository.countByCreatedDateAfter(localDateTime);
        return reviews;
    }
}
