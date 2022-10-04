package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.Exception;
import com.onulstore.domain.brand.Brand;
import com.onulstore.domain.brand.BrandRepository;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.ErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.web.dto.BrandDto;
import com.onulstore.web.dto.ProductDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class BrandService {

    private final MemberRepository memberRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    /**
     * 브랜드 전체 조회
     *
     * @return 브랜드 전체 정보
     */
    @Transactional(readOnly = true)
    public Map<String, List<Brand>> getBrands() {
        Map<String, List<Brand>> resultMap = new HashMap<>();

        List<Brand> findAllBrands = brandRepository.findAll();
        resultMap.put("findAllBrands", findAllBrands);
        return resultMap;
    }

    /**
     * BrandId로 상품 조회
     *
     * @param brandId
     * @param pageable
     *
     * @return 해당 Brand 상품 전체 정보
     */
    @Transactional(readOnly = true)
    public Page<ProductDto.ProductResponse> findProductByBrand(Long brandId, Pageable pageable) {
        Brand brand = brandRepository.findById(brandId).orElseThrow(
            () -> new Exception(ErrorResult.BRAND_NOT_FOUND));

        return productRepository.findByBrandId(brand.getId(), pageable)
            .map(ProductDto.ProductResponse::of);
    }

    /**
     * 브랜드 등록
     *
     * @param brandRequest
     *
     * @return 브랜드 등록 정보
     */
    public BrandDto.BrandResponse addBrand(BrandDto.BrandRequest brandRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        return BrandDto.BrandResponse.of(
            brandRepository.save(new Brand(brandRequest.getBrandName())));
    }

    /**
     * 브랜드 수정
     *
     * @param updateRequest
     * @param brandId
     *
     * @return 수정된 브랜드 정보
     */
    public BrandDto.BrandResponse updateBrand(BrandDto.UpdateRequest updateRequest, Long brandId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Brand brand = brandRepository.findById(brandId).orElseThrow(
            () -> new Exception(ErrorResult.BRAND_NOT_FOUND));
        Brand updateBrand = brand.updateBrand(updateRequest);
        return BrandDto.BrandResponse.of(brandRepository.save(updateBrand));
    }

    /**
     * 브랜드 삭제
     *
     * @param brandId
     */
    public void deleteBrand(Long brandId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Brand brand = brandRepository.findById(brandId).orElseThrow(
            () -> new Exception(ErrorResult.BRAND_NOT_FOUND));
        brandRepository.delete(brand);
    }

}
