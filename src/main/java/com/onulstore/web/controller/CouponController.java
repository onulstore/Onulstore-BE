package com.onulstore.web.controller;

import com.onulstore.service.CouponService;
import com.onulstore.web.dto.CouponDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Coupon-Controller"})
public class CouponController {

    private final CouponService couponService;

    @ApiOperation(value = "모두에게 쿠폰 등록")
    @PostMapping("/coupons")
    public ResponseEntity<String> couponToAll(@RequestBody CouponDto.RequestCoupon requestDto) {
        couponService.allUser(requestDto);
        return ResponseEntity.ok("쿠폰 등록이 완료되었습니다.");
    }

    @ApiOperation(value = "특정 유저에게 쿠폰 등록")
    @PostMapping("/coupon")
    public ResponseEntity<String> couponToSpecificUser(@RequestBody CouponDto.RequestCoupon requestDto) {
        couponService.specificUser(requestDto);
        return ResponseEntity.ok("쿠폰 등록이 완료되었습니다.");
    }

    @ApiOperation(value = "쿠폰 조회")
    @GetMapping("/mycoupons")
    public ResponseEntity<List<CouponDto.ResponseCoupon>> myCoupons() {
        return ResponseEntity.ok(couponService.myCoupons());
    }
}
