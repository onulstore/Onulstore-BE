package com.onulstore.service;

import com.onulstore.config.exception.Exception;
import com.onulstore.domain.category.Category;
import com.onulstore.domain.category.CategoryRepository;
import com.onulstore.domain.enums.ErrorResult;
import com.onulstore.web.dto.CategoryDto;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public HashMap<String, Object> getCategories() {
        HashMap<String, Object> resultMap = new HashMap<>();

        List<Category> findAllCategory = categoryRepository.findAll();
        resultMap.put("readAll", findAllCategory);
        return resultMap;
    }

    @Transactional
    public void addCategory(CategoryDto.CategoryRequest categoryRequest) {
        Category parent = Optional.ofNullable(categoryRequest.getParentId())
            .map(id -> categoryRepository.findById(id).orElseThrow(() -> new Exception(
                ErrorResult.CATEGORY_NOT_FOUND)))
            .orElse(null);
        categoryRepository.save(new Category(categoryRequest.getCategoryName(), parent));
    }

    @Transactional
    public CategoryDto.CategoryResponse updateCategory(
        CategoryDto.updateCatRequest updateCatRequest,
        Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
            () -> new Exception(ErrorResult.CATEGORY_NOT_FOUND));
        Category updateCategory = category.updateCategory(updateCatRequest);
        return CategoryDto.CategoryResponse.of(categoryRepository.save(updateCategory));
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
            () -> new Exception(ErrorResult.CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
    }
}


