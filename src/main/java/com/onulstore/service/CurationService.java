package com.onulstore.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onulstore.config.SecurityUtil;
import com.onulstore.domain.curation.Curation;
import com.onulstore.domain.curation.CurationProduct;
import com.onulstore.domain.curation.CurationProductRepository;
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
import java.util.ArrayList;
import java.util.List;
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
    private final CurationProductRepository curationProductRepository;

    /**
     * Magazine 내용 등록
     * @param magazineRequest
     * @return Magazine 내용 등록 정보
     */
    public CurationDto.CurationResponse createMagazine(CurationDto.MagazineRequest magazineRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
        }

        Curation curation = Curation.createMagazine(magazineRequest.getTitle(), magazineRequest.getContent(),
                magazineRequest.getCurationImg(), member);

        return CurationDto.CurationResponse.of(curationRepository.save(curation));
    }

    /**
     * Magazine 상품 등록
     * @param addProductRequest
     * @return curationProduct.getId()
     */
    public Long addProductIntoMagazine(CurationDto.AddProductRequest addProductRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
        }

        Product product = productRepository.findById(addProductRequest.getProductId()).orElseThrow(
                () -> new UserException(UserErrorResult.PRODUCT_NOT_FOUND));
        Curation curation = curationRepository.findById(addProductRequest.getCurationId()).orElseThrow();

        CurationProduct curationProduct = CurationProduct.addProductMagazine(curation, product);
        curationProductRepository.save(curationProduct);
        return curationProduct.getId();
    }

    /**
     * MD Recommend 등록
     * @param recommendRequest
     * @return curation.getId()
     */
    public Long createRecommend(CurationDto.RecommendRequest recommendRequest) {
        List<CurationProduct> curationProductList = new ArrayList<>();
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
        }

        Product product = productRepository.findById(recommendRequest.getProductId()).orElseThrow(
                () -> new UserException(UserErrorResult.PRODUCT_NOT_FOUND));

        curationProductList.add(CurationProduct.createCurationProduct(product));

        Curation curation = Curation.createRecommend(recommendRequest.getTitle(), recommendRequest.getContent(),
                recommendRequest.getCurationImg(), member, curationProductList);
        curationRepository.save(curation);


        return curation.getId();
    }

    /**
     * 특정 큐레이션 정보 조회
     * @param curationId
     * @return 해당 curation 정보
     */   
    @Transactional(readOnly = true)
    public List<CurationProduct> getCurationList(Long curationId) {
        Curation curation = curationRepository.findById(curationId).orElseThrow();
        return curationProductRepository.findAllByCuration(curation);
    }
    
    /**
     * Curation 전체 조회
     * @param pageable
     * @return 전체 Curation 정보
     */
    @Transactional(readOnly = true)
    public Page<CurationDto.CurationResponse> getCuration(Pageable pageable) {
        return curationRepository.findAll(pageable).map(CurationDto.CurationResponse::of);
    }

    /**
     * Curation 삭제
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

    /**
     * Curation 수정
     * @param updateCuration
     * @param curationId
     * @return 수정된 Curation 내용
     */
    public CurationDto.CurationResponse updateCuration(CurationDto.UpdateCuration updateCuration, Long curationId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
        }

        Curation findCuration = curationRepository.findById(curationId).orElseThrow(
                () -> new UserException(UserErrorResult.CURATION_NOT_FOUND));
        Curation curation = findCuration.updateCuration(updateCuration);

        return CurationDto.CurationResponse.of(curationRepository.save(curation));
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
