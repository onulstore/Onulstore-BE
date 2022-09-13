package com.onulstore.web.controller;

import com.onulstore.service.ProductService;
import com.onulstore.web.dto.ProductDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = {"상품 Controller"})
public class ProductController {

    private final ProductService productService;

    @ApiOperation(value = "상품 등록")
    @PostMapping("/products")
    public ResponseEntity<ProductDto.ProductResponse> registerProduct(@RequestBody ProductDto.ProductRequest requestDto){
        return ResponseEntity.ok(productService.register(requestDto));
    }

    @ApiOperation(value = "상품 수정")
    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDto.ProductResponse> modifyProduct(@PathVariable Long productId, @RequestBody ProductDto.ProductRequest requestDto){
        return ResponseEntity.ok(productService.modify(requestDto, productId));
    }



}
