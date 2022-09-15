package com.onulstore.web.controller;

import com.onulstore.service.WishlistService;
import com.onulstore.web.dto.WishlistDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Wishlist Controller"})
public class WishlistController {

    private final WishlistService wishlistService;

    // 찜 등록
    @ApiOperation(value = "찜 등록")
    @PostMapping("/wishlists")
    public ResponseEntity<WishlistDto.WishlistResponse> addWishlist(@RequestBody WishlistDto.WishlistRequest request) {

        return ResponseEntity.ok(wishlistService.addWishlist(request));
    }

    // 찜 삭제
    @ApiOperation(value = "찜 삭제")
    @DeleteMapping("/wishlists/{wishlistId}")
    public void deleteWishlist(@PathVariable Long wishlistId) {
        wishlistService.deleteWishlist(wishlistId);
    }






}
