package com.onulstore.web.controller;

import com.onulstore.domain.brand.Brand;
import com.onulstore.service.BrandService;
import com.onulstore.web.dto.BrandDto;
import com.onulstore.web.dto.ProductDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@Api(tags = {"Brand-Controller"})
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    @ApiOperation(value = "모든 브랜드 조회")
    public ResponseEntity<Map<String, List<Brand>>> getBrandProducts() {
        return ResponseEntity.ok(brandService.getBrands());
    }

    @PostMapping
    @ApiOperation(value = "브랜드 등록 / 인증 필요(관리자)")
    public ResponseEntity<BrandDto.BrandResponse> addBrand(
        @RequestBody BrandDto.BrandRequest brandRequest) {
        return ResponseEntity.ok(brandService.addBrand(brandRequest));
    }

    @PutMapping("/{brandId}")
    @ApiOperation(value = "브랜드 수정 / 인증 필요(관리자)")
    public ResponseEntity<BrandDto.BrandResponse> updateBrand(
        @RequestBody BrandDto.UpdateRequest updateRequest,
        @ApiParam(required = true) @PathVariable Long brandId) {
        return ResponseEntity.ok(brandService.updateBrand(updateRequest, brandId));
    }

    @DeleteMapping("/{brandId}")
    @ApiOperation(value = "브랜드 삭제 / 인증 필요(관리자)")
    public ResponseEntity<String> deleteBrand(
        @ApiParam(required = true) @PathVariable Long brandId) {
        brandService.deleteBrand(brandId);
        return ResponseEntity.ok("브랜드가 삭제되었습니다.");
    }

    @GetMapping("/{brandId}/product")
    @ApiOperation(value = "브랜드로 상품 조회")
    public ResponseEntity<Page<ProductDto.ProductResponse>> findByBrandId(
        @ApiParam(required = true) @PathVariable Long brandId, Pageable pageable) {
        return ResponseEntity.ok(brandService.findProductByBrand(brandId, pageable));
    }

}
