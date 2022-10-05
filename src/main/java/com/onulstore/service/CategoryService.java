package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.Exception;
import com.onulstore.domain.category.Category;
import com.onulstore.domain.category.CategoryRepository;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.domain.enums.ErrorResult;
import com.onulstore.web.dto.CategoryDto;
import com.onulstore.web.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 전체 조회
     * @return 카테고리 전체 정보
     */
    @Transactional(readOnly = true)
    public Map<String, List<Category>> getCategories() {
        Map<String, List<Category>> resultMap = new HashMap<>();

        List<Category> findAllCategory = categoryRepository.findAll();
        resultMap.put("findAllCategory", findAllCategory);
        return resultMap;
    }

    /**
     * 카테고리 등록
     * @param categoryRequest
     */
    public void addCategory(CategoryDto.CategoryRequest categoryRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Category parent = Optional.ofNullable(categoryRequest.getParentId())
            .map(id -> categoryRepository.findById(id).orElseThrow(
                () -> new Exception(ErrorResult.CATEGORY_NOT_FOUND)))
            .orElse(null);
        categoryRepository.save(new Category(categoryRequest.getCategoryName(), parent));
    }

    /**
     * 카테고리 수정
     * @param updateCatRequest
     * @param categoryId
     * @return 수정된 카테고리 정보
     */
    public CategoryDto.CategoryResponse updateCategory(
        CategoryDto.updateCatRequest updateCatRequest, Long categoryId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Category category = categoryRepository.findById(categoryId).orElseThrow(
            () -> new Exception(ErrorResult.CATEGORY_NOT_FOUND));
        Category updateCategory = category.updateCategory(updateCatRequest);
        return CategoryDto.CategoryResponse.of(categoryRepository.save(updateCategory));
    }

    /**
     * 카테고리 삭제
     * @param categoryId
     */
    public void deleteCategory(Long categoryId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Category category = categoryRepository.findById(categoryId).orElseThrow(
            () -> new Exception(ErrorResult.CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
    }

    /**
     * categoryId로 상품 조회
     * @param categoryId
     * @param pageable
     * @return categoryId로 조회한 상품 목록(2depth 까지)
     */
    public Page<ProductDto.ProductResponse> getCategoryById(Long categoryId, Pageable pageable) {
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(
            () -> new Exception(ErrorResult.CATEGORY_NOT_FOUND));

        List<ProductDto.ProductResponse> findProductByCategory = new ArrayList<>();
        List<Category> findAllCategory = categoryRepository.findAllByParentId(categoryId);
        findAllCategory.add(findCategory);
        for (Category category : findAllCategory) {
            Product product = productRepository.findByCategoryId(category.getId()).orElseThrow();
            findProductByCategory.add(ProductDto.ProductResponse.of(product));
        }
        return new PageImpl<>(findProductByCategory, pageable, findAllCategory.size());
    }

}


