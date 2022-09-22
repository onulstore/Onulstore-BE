package com.onulstore.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onulstore.config.SecurityUtil;
import com.onulstore.domain.curation.Curation;
import com.onulstore.domain.curation.CurationRepository;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.CurationForm;
import com.onulstore.domain.enums.UserErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.exception.UserException;
import com.onulstore.web.dto.CurationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class CurationService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client s3Client;
    private final CurationRepository curationRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    /**
     * Magazine 등록
     * @param curationRequest
     * @return curation.getId()
     */
    public Long createMagazine(CurationDto.CurationRequest curationRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
        }

        Product product = productRepository.findById(curationRequest.getProductId()).orElseThrow(
                () -> new UserException(UserErrorResult.PRODUCT_NOT_FOUND));

        Curation curation = Curation.createCurationM(curationRequest.getTitle(), curationRequest.getContent(),
                curationRequest.getCurationImg(), member, product);
        curationRepository.save(curation);

        return curation.getId();
    }

    /**
     * Recommend 등록
     * @param curationRequest
     * @return curation.getId()
     */
    public Long createRecommend(CurationDto.CurationRequest curationRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
        }

        Product product = productRepository.findById(curationRequest.getProductId()).orElseThrow(
                () -> new UserException(UserErrorResult.PRODUCT_NOT_FOUND));

        Curation curation = Curation.createCurationR(curationRequest.getTitle(), curationRequest.getContent(),
                curationRequest.getCurationImg(), member, product);
        curationRepository.save(curation);

        return curation.getId();
    }

    /**
     * Curation 전체 조회
     * @param pageable
     * @return PageImpl<>(curationResponses, pageable, totalCount)
     */
    @Transactional(readOnly = true)
    public Page<CurationDto.CurationResponse> getCuration(Pageable pageable) {
        return curationRepository.findAll(pageable).map(CurationDto.CurationResponse::of);
    }

    /**
     * Curation 등록 상품 삭제
     * @param curationId
     */
    public void deleteCuration(Long curationId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
        }
        Curation curation = curationRepository.findById(curationId).orElseThrow(
                () -> new UserException(UserErrorResult.CURATION_NOT_FOUND));
        curationRepository.delete(curation);
    }

    /**
     * Magazine 전체 조회
     * @param pageable
     * @return Magazine
     */
    @Transactional(readOnly = true)
    public Page<CurationDto.CurationResponse> getMagazine(Pageable pageable) {
        return curationRepository.findAllByCurationForm(CurationForm.MAGAZINE.getKey(), pageable)
                .map(CurationDto.CurationResponse::of);
    }

    /**
     * Recommend 전체 조회
     * @param pageable
     * @return Recommend
     */
    @Transactional(readOnly = true)
    public Page<CurationDto.CurationResponse> getRecommend(Pageable pageable) {
        return curationRepository.findAllByCurationForm(CurationForm.RECOMMEND.getKey(), pageable)
                .map(CurationDto.CurationResponse::of);
    }

    public String upload(InputStream inputStream, String originFileName) {
        String s3FileName = UUID.randomUUID() + "-" + originFileName;
        ObjectMetadata objMeta = new ObjectMetadata();
        s3Client.putObject(bucket, s3FileName, inputStream, objMeta);

        return s3FileName;
    }

    public void addImage(Long curationId, String image) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
        }

        Curation curation = curationRepository.findById(curationId).orElseThrow(
                () -> new UserException(UserErrorResult.CURATION_NOT_FOUND));
        curation.insertImage(image);
    }

}
