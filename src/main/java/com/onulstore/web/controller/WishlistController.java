package com.onulstore.web.controller;

import com.onulstore.service.WishlistService;
import com.onulstore.web.dto.ProductDto;
import com.onulstore.web.dto.WishlistDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlists")
@Api(tags = {"Wishlist-Controller"})
public class WishlistController {

    private final WishlistService wishlistService;

    @ApiOperation(value = "찜 조회 / 인증 필요")
    @GetMapping
    public ResponseEntity<List<ProductDto.ProductRes>> getWishlist() {
        return ResponseEntity.ok(wishlistService.getWishlist());
    }

    @ApiOperation(value = "찜 등록 / 인증 필요")
    @PostMapping
    public ResponseEntity<ProductDto.ProductRes> addWishlist(
        @RequestBody WishlistDto.WishlistRequest request) {
        return ResponseEntity.ok(wishlistService.addWishlist(request));
    }

    @ApiOperation(value = "찜 삭제 / 인증 필요")
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteWishlist(@PathVariable Long productId) {
        wishlistService.deleteWishlist(productId);
        return ResponseEntity.ok("해당 위시리스트가 삭제되었습니다.");
    }

}