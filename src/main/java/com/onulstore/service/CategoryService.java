package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.domain.category.Category;
import com.onulstore.domain.category.CategoryRepository;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.UserErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.exception.UserException;
import com.onulstore.web.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 전체 조회
     * @return 카테고리 전체 정보
     */
    @Transactional(readOnly = true)
    public HashMap<String, Object> getCategories() {
        HashMap<String, Object> resultMap = new HashMap<>();

        List<Category> findAllCategory = categoryRepository.findAll();
        resultMap.put("readAll", findAllCategory);
        return resultMap;
    }

    /**
     * 카테고리 등록
     * @param categoryRequest
     */
    public void addCategory(CategoryDto.CategoryRequest categoryRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
        }

        Category parent = Optional.ofNullable(categoryRequest.getParentId())
                .map(id -> categoryRepository.findById(id).orElseThrow(
                        () -> new UserException(UserErrorResult.CATEGORY_NOT_FOUND)))
                .orElse(null);
        categoryRepository.save(new Category(categoryRequest.getCategoryName(), parent));
    }

    /**
     * 카테고리 수정
     * @param updateCatRequest
     * @param categoryId
     * @return 수정된 카테고리 정보
     */
    public CategoryDto.CategoryResponse updateCategory(CategoryDto.updateCatRequest updateCatRequest, Long categoryId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
        }

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new UserException(UserErrorResult.CATEGORY_NOT_FOUND));
        Category updateCategory = category.updateCategory(updateCatRequest);
        return CategoryDto.CategoryResponse.of(categoryRepository.save(updateCategory));
    }

    /**
     * 카테고리 삭제
     * @param categoryId
     */
    public void deleteCategory(Long categoryId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
        }

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new UserException(UserErrorResult.CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
    }
}


