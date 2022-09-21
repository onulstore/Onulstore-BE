package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.domain.curation.Curation;
import com.onulstore.domain.curation.CurationProduct;
import com.onulstore.domain.curation.CurationRepository;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.exception.AccessPrivilegeExceptions;
import com.onulstore.exception.NotExistUserException;
import com.onulstore.exception.ProductNotFoundException;
import com.onulstore.web.dto.CurationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CurationService {

    private final CurationRepository curationRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    /**
     * Magazine 등록
     * @param curationRequest
     * @return curation.getId()
     */
    public Long createMagazine(CurationDto.CurationRequest curationRequest) {
        List<CurationProduct> curationProductList = new ArrayList<>();

        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new AccessPrivilegeExceptions("접근 권한이 없습니다.");
        }

        Product product = productRepository.findById(curationRequest.getProductId()).orElseThrow(
                () -> new ProductNotFoundException("해당 상품이 존재하지 않습니다."));
        curationProductList.add(CurationProduct.createCurationProduct(
                curationRequest.getTitle(), curationRequest.getContent(), curationRequest.getCurationImg(), product));

        Curation curation = Curation.createCurationM(member, curationProductList);
        curationRepository.save(curation);

        return curation.getId();
    }

    /**
     * Recommend 등록
     * @param curationRequest
     * @return curation.getId()
     */
    public Long createRecommend(CurationDto.CurationRequest curationRequest) {
        List<CurationProduct> curationProducts = new ArrayList<>();

        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new AccessPrivilegeExceptions("접근 권한이 없습니다.");
        }

        Product product = productRepository.findById(curationRequest.getProductId()).orElseThrow(
                () -> new ProductNotFoundException("해당 상품이 존재하지 않습니다."));
        curationProducts.add(CurationProduct.createCurationProduct(
                curationRequest.getTitle(), curationRequest.getContent(), curationRequest.getCurationImg(), product));

        Curation curation = Curation.createCurationR(member, curationProducts);
        curationRepository.save(curation);

        return curation.getId();
    }

    /**
     * Curation 조회
     * @param pageable
     * @return PageImpl<>(curationResponses, pageable, totalCount)
     */
    @Transactional(readOnly = true)
    public Page<CurationDto.CurationResponse> getCuration(Pageable pageable) {
        List<CurationDto.CurationResponse> curationResponses = new ArrayList<>();
        Member member = memberRepository.findById(1L).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));

        List<Curation> curationList = curationRepository.findCurations(member.getEmail(), pageable);
        Long totalCount = curationRepository.countCuration(member.getEmail());

        for (Curation curation : curationList) {
            CurationDto.CurationResponse curationResponse = new CurationDto.CurationResponse(curation);
            List<CurationProduct> curationProductList = curation.getCurationProducts();
            for (CurationProduct curationProduct : curationProductList) {
                CurationDto.CurationProduct curationProductDto = new CurationDto.CurationProduct(curationProduct);
                curationResponse.addCurationProduct(curationProductDto);
            }
            curationResponses.add(curationResponse);
        }
        return new PageImpl<>(curationResponses, pageable, totalCount);
    }

    /**
     * Curation 등록 상품 삭제
     * @param curationId
     */
    public void deleteCuration(Long curationId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new AccessPrivilegeExceptions("접근 권한이 없습니다.");
        }
        curationRepository.deleteById(curationId);
    }



}
