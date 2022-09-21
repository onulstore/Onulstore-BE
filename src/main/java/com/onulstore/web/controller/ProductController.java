package com.onulstore.web.controller;

import com.onulstore.service.ProductService;
import com.onulstore.web.dto.ProductDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Product Controller"})
public class ProductController {

    private final ProductService productService;

    @ApiOperation(value = "상품 등록")
    @PostMapping("/products")
    public ResponseEntity<ProductDto.ProductResponse> registerProduct(@RequestBody ProductDto.ProductRequest requestDto){
        return ResponseEntity.ok(productService.register(requestDto));
    }

    @ApiOperation(value = "상품 수정")
    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDto.ProductResponse> modifyProduct(@PathVariable Long productId, @RequestBody ProductDto.modifyRequest modifyRequest){
        return ResponseEntity.ok(productService.modify(modifyRequest, productId));
    }

    @ApiOperation(value = "상품 삭제")
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        productService.delete(productId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "상품 상세 조회")
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDto.ProductResponse> detailInquiryProduct(@PathVariable Long productId, HttpServletRequest request){
        return ResponseEntity.ok(productService.detailInquiry(productId, request));
    }

    @ApiOperation(value = "상품 전체 조회")
    @GetMapping("/products/list")
    public ResponseEntity<Page<ProductDto.ProductResponse>> entireProducts(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size=5) Pageable pageable){
        return ResponseEntity.ok(productService.entireProductList(pageable));
    }

    @ApiOperation(value = "상품 검색")
    @GetMapping("/products/search") // 보류
    public ResponseEntity<Page<ProductDto.ProductResponse>> searchProducts(@PageableDefault(sort = "id", direction = Sort.Direction.DESC, size=5) Pageable pageable){
        return ResponseEntity.ok(productService.entireProductList(pageable));
    }

    @ApiOperation(value = "상품 이미지 업로드")
    @PostMapping("/products/{productId}/image")
    public ResponseEntity uploadFile(@RequestParam("images") MultipartFile multipartFile, @PathVariable Long productId) throws IOException {
        String image = productService.upload(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        productService.addImage(productId, image);
        return new ResponseEntity(HttpStatus.OK);
    }

}