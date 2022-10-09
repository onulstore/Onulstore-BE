package com.onulstore.web.controller;

import com.onulstore.service.ReviewService;
import com.onulstore.web.dto.ReviewDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Review-Controller"})
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @ApiOperation(value = "리뷰 등록")
    @PostMapping("/reviews/{orderId}")
    public ResponseEntity<ReviewDto.ReviewResponse> insertReview(
        @RequestBody ReviewDto.ReviewRequest request, @PathVariable Long orderId) {
        return ResponseEntity.ok(reviewService.insertReview(request, orderId));
    }

    // 리뷰 수정
    @ApiOperation(value = "리뷰 수정")
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewDto.ReviewResponse> updateReview(@PathVariable Long reviewId,
        @RequestBody ReviewDto.ReviewRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, request));
    }

    // 리뷰 삭제
    @ApiOperation(value = "리뷰 삭제")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("해당 리뷰가 삭제되었습니다.");
    }

    // 리뷰 상세 조회
    @ApiOperation(value = "리뷰 상세 조회")
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewDto.ReviewResponse> getReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReview(reviewId));
    }

    // 리뷰 목록 조회(사용자별)
    @ApiOperation(value = "리뷰 목록 조회(멤버별)")
    @GetMapping("/members/reviews")
    public ResponseEntity<List<ReviewDto.ReviewResponse>> getMemberReviewList() {
        return ResponseEntity.ok(reviewService.getMemberReviewList());
    }

    // 리뷰 목록 조회(상품별)
    @ApiOperation(value = "리뷰 목록 조회(상품별)")
    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<Page<ReviewDto.ReviewResponse>> getProductReviewList(
        @PathVariable Long productId,
        @PageableDefault(sort = "id", size = 10, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(reviewService.getProductReviewList(productId, pageable));
    }

    // 리뷰 이미지 등록
    @ApiOperation(value = "리뷰 이미지 등록")
    @PostMapping("/reviews/{reviewId}/image")
    public ResponseEntity<String> uploadImage(
        @RequestParam("images") List<MultipartFile> multipartFiles, @PathVariable Long reviewId)
        throws IOException {
        reviewService.uploadImages(multipartFiles, reviewId);
        return ResponseEntity.ok("리뷰 이미지가 등록되었습니다.");
    }

}
