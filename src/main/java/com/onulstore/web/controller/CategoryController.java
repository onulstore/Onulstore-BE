package com.onulstore.web.controller;

import com.onulstore.service.CategoryService;
import com.onulstore.web.dto.CategoryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Api(tags = {"Category-Controller"})
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @ApiOperation(value = "모든 카테고리 조회")
    public ResponseEntity<HashMap<String, Object>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @PostMapping
    @ApiOperation(value = "카테고리 생성")
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryDto.CategoryRequest categoryRequest) {
        categoryService.addCategory(categoryRequest);
        return ResponseEntity.ok("카테고리가 등록되었습니다.");
    }

    @PutMapping("/{categoryId}")
    @ApiOperation(value = "카테고리 수정")
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryDto.updateCatRequest updateCatRequest,
                                              @ApiParam(required = true)
                                              @PathVariable Long categoryId) {
        categoryService.updateCategory(updateCatRequest, categoryId);
        return ResponseEntity.ok("카테고리가 수정되었습니다.");
    }

    @DeleteMapping("/{categoryId}")
    @ApiOperation(value = "카테고리 삭제")
    public ResponseEntity<String> deleteCategory(@ApiParam(required = true) @PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("카테고리가 삭제되었습니다.");
    }

}
