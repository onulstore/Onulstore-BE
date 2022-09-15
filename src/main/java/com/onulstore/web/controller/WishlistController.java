package com.onulstore.web.controller;

import com.onulstore.domain.wishlist.Wishlist;
import com.onulstore.service.WishlistService;
import com.onulstore.web.dto.ProductDto;
import com.onulstore.web.dto.WishlistDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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

    // 찜 조회
    @ApiOperation(value = "찜 조회")
    @GetMapping("/wishlists")
    public ResponseEntity<HashMap<String, Object>> getWishlist() {
        return ResponseEntity.ok(wishlistService.getWishlist());
    }

}