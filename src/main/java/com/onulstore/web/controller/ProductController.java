package com.onulstore.web.controller;

import com.onulstore.service.ProductService;
import com.onulstore.web.dto.ProductDto;
import com.onulstore.web.dto.ProductDto.DiscountProductDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Product-Controller"})
public class ProductController {

    private final ProductService productService;

    @ApiOperation(value = "상품 등록  / 인증 필요(관리자)")
    @PostMapping("/products")
    public ResponseEntity<ProductDto.ProductResponse> registerProduct(
        @RequestBody ProductDto.ProductRequest requestDto) {
        return ResponseEntity.ok(productService.register(requestDto));
    }

    @ApiOperation(value = "상품 수정  / 인증 필요(관리자)")
    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDto.ProductResponse> modifyProduct(@PathVariable Long productId,
        @RequestBody ProductDto.ProductRequest modifyRequest) {
        return ResponseEntity.ok(productService.modify(modifyRequest, productId));
    }

    @ApiOperation(value = "상품 삭제  / 인증 필요(관리자)")
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.delete(productId);
        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }

    @ApiOperation(value = "상품 상세 조회")
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDto.ProductResponse> detailInquiryProduct(
        @PathVariable Long productId, HttpServletRequest request) {
        return ResponseEntity.ok(productService.detailInquiry(productId, request));
    }

    @ApiOperation(value = "상품 전체 조회")
    @GetMapping("/products/list")
    public ResponseEntity<Page<ProductDto.ProductResponse>> entireProducts(
        @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 32) Pageable pageable) {
        return ResponseEntity.ok(productService.entireProductList(pageable));
    }

    @ApiOperation(value = "상품 검색")
    @PostMapping("/products/search")
    public ResponseEntity<Page<ProductDto.ProductResponse>> searchProducts(
        @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 32) Pageable pageable,
        @RequestParam String product) {
        return ResponseEntity.ok(productService.searchProduct(pageable, product));
    }

    @ApiOperation(value = "상품 이미지 업로드  / 인증 필요(관리자)")
    @PostMapping("/products/{productId}/image")
    public ResponseEntity uploadImage(@RequestParam("images") List<MultipartFile> multipartFile,
        @PathVariable Long productId) throws IOException {
        productService.uploadImages(multipartFile, productId);
        return ResponseEntity.ok("상품 이미지가 등록되었습니다.");
    }

    @ApiOperation(value = "상품 설명 업로드  / 인증 필요(관리자)")
    @PostMapping("/products/{productId}/content")
    public ResponseEntity<String> uploadContent(@RequestParam("images") MultipartFile multipartFile,
        @PathVariable Long productId) throws IOException {
        return ResponseEntity.ok(productService.uploadContent(multipartFile, productId));
    }

    @ApiOperation(value = "상품 이미지 제거  / 인증 필요(관리자)")
    @DeleteMapping("/products/{productId}/image")
    public ResponseEntity deleteImage(@PathVariable Long productId) {
        productService.deleteImage(productId);
        return ResponseEntity.ok("상품 이미지가 제거되었습니다.");
    }

    @ApiOperation(value = "상품 할인 추가  / 인증 필요(관리자)")
    @PostMapping("/products/discount/{productId}")
    public ResponseEntity discountProduct(@RequestBody DiscountProductDto discountProductDto) {
        productService.discountProduct(discountProductDto);
        return ResponseEntity.ok("할인이 적용되었습니다.");
    }
}