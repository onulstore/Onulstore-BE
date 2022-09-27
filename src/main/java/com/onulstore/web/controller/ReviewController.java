package com.onulstore.web.controller;

import com.onulstore.service.ReviewService;
import com.onulstore.web.dto.ReviewDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Review Controller"})
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @ApiOperation(value = "리뷰 등록")
    @PostMapping("/reviews")
    public ResponseEntity<ReviewDto.ReviewResponse> insertReview(@RequestBody ReviewDto.ReviewRequest request) {
        return ResponseEntity.ok(reviewService.insertReview(request));
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
}
