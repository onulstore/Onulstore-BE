package com.onulstore.web.controller;

import com.onulstore.domain.category.Category;
import com.onulstore.service.CategoryService;
import com.onulstore.web.dto.CategoryDto;
import com.onulstore.web.dto.ProductDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Api(tags = {"Category-Controller"})
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @ApiOperation(value = "모든 카테고리 조회")
    public ResponseEntity<Map<String, List<Category>>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @PostMapping
    @ApiOperation(value = "카테고리 생성 / 인증 필요(관리자)")
    public ResponseEntity<String> addCategory(
        @Valid @RequestBody CategoryDto.CategoryRequest categoryRequest) {
        categoryService.addCategory(categoryRequest);
        return ResponseEntity.ok("카테고리가 등록되었습니다.");
    }

    @PutMapping("/{categoryId}")
    @ApiOperation(value = "카테고리 수정 / 인증 필요(관리자)")
    public ResponseEntity<String> addCategory(
        @Valid @RequestBody CategoryDto.updateCatRequest updateCatRequest,
        @ApiParam(required = true)
        @PathVariable Long categoryId) {
        categoryService.updateCategory(updateCatRequest, categoryId);
        return ResponseEntity.ok("카테고리가 수정되었습니다.");
    }

    @DeleteMapping("/{categoryId}")
    @ApiOperation(value = "카테고리 삭제 / 인증 필요(관리자)")
    public ResponseEntity<String> deleteCategory(
        @ApiParam(required = true) @PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("카테고리가 삭제되었습니다.");
    }

    @GetMapping("/{categoryId}/products")
    @ApiOperation(value = "카테고리 아이디로 상품 조회(2depth 까지)")
    public ResponseEntity<Page<List<ProductDto.ProductResponse>>> getCategoryById(
        @ApiParam(required = true) @PathVariable Long categoryId,
        @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 8) Pageable pageable) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId, pageable));
    }

}
