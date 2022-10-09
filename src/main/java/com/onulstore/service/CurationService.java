package com.onulstore.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.Exception;
import com.onulstore.domain.curation.Curation;
import com.onulstore.domain.curation.CurationProduct;
import com.onulstore.domain.curation.CurationRepository;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.CurationForm;
import com.onulstore.domain.enums.ErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.web.dto.CurationDto;
import com.onulstore.web.dto.CurationDto.CurationInfo;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


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
     * @param magazineRequest
     * @return Magazine 내용 등록 정보
     */
    public void createMagazine(CurationDto.MagazineRequest magazineRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        List<Product> products = new ArrayList<>();
        List<CurationProduct> curationProducts = new ArrayList<>();

        for (Long product : magazineRequest.getProductList()) {
            products.add(productRepository.findById(product).orElseThrow(
                () -> new Exception(ErrorResult.PRODUCT_NOT_FOUND)));
        }

        for (Product product : products) {
            curationProducts.add(CurationProduct.createCurationProduct(product));
        }

        Curation curation = Curation.createMagazine(magazineRequest.getTitle(),
            magazineRequest.getContent(), member, curationProducts);

        curationRepository.save(curation);
    }

    /**
     * MD Recommend 등록
     * @param recommendRequest
     * @return curation.getId()
     */
    public void createRecommend(CurationDto.RecommendRequest recommendRequest) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Product product = productRepository.findById(recommendRequest.getProductId()).orElseThrow(
            () -> new Exception(ErrorResult.PRODUCT_NOT_FOUND));

        CurationProduct curationProduct = CurationProduct.createCurationProduct(product);

        Curation curation = Curation.createRecommend(recommendRequest.getTitle(),
            recommendRequest.getContent(), member, curationProduct);
        curationRepository.save(curation);
    }

    /**
     * 특정 큐레이션 정보 조회
     * @param curationId
     * @return 해당 curation 정보
     */
    @Transactional(readOnly = true)
    public CurationDto.CurationInfo getCurationList(Long curationId) {
        Curation curation = curationRepository.findById(curationId).orElseThrow(
            () -> new Exception(ErrorResult.CURATION_NOT_FOUND));
        CurationDto.CurationInfo curationInfo = new CurationInfo(curation);
        List<CurationProduct> curationProductList = curation.getCurationProducts();
        for (CurationProduct curationProduct : curationProductList) {
            CurationDto.CurationProduct curationProductDto = new CurationDto.CurationProduct(
                curationProduct);
            curationInfo.addCurationProduct(curationProductDto);
        }
        return curationInfo;
    }

    /**
     * Curation 전체 조회
     * @param pageable
     * @return 전체 Curation 정보
     */
    @Transactional(readOnly = true)
    public Page<CurationDto.CurationInfo> getCuration(Pageable pageable) {
        List<Curation> curations = curationRepository.findAll();
        List<CurationDto.CurationInfo> curationInfos = new ArrayList<>();

        for (Curation curation : curations) {
            CurationDto.CurationInfo curationInfo = new CurationInfo(curation);
            List<CurationProduct> curationProductList = curation.getCurationProducts();
            for (CurationProduct curationProduct : curationProductList) {
                CurationDto.CurationProduct curationProductDto = new CurationDto.CurationProduct(
                    curationProduct);
                curationInfo.addCurationProduct(curationProductDto);
            }
            curationInfos.add(curationInfo);
        }
        return new PageImpl<>(curationInfos, pageable, curations.size());
    }

    /**
     * Curation 삭제
     * @param curationId
     */
    public void deleteCuration(Long curationId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }
        Curation curation = curationRepository.findById(curationId).orElseThrow(
            () -> new Exception(ErrorResult.CURATION_NOT_FOUND));
        curationRepository.delete(curation);
    }

    /**
     * Magazine 전체 조회
     * @param pageable
     * @return Magazine
     */
    @Transactional(readOnly = true)
    public Page<CurationDto.CurationInfo> getMagazine(Pageable pageable) {
        List<Curation> curations = curationRepository.findCurations(
            CurationForm.MAGAZINE.getKey(), pageable);
        Long totalCount = curationRepository.countCuration(CurationForm.MAGAZINE.getKey());

        List<CurationDto.CurationInfo> curationInfos = new ArrayList<>();
        for (Curation curation : curations) {
            CurationDto.CurationInfo curationInfo = new CurationInfo(curation);
            List<CurationProduct> curationProductList = curation.getCurationProducts();
            for (CurationProduct curationProduct : curationProductList) {
                CurationDto.CurationProduct curationProductDto = new CurationDto.CurationProduct(
                    curationProduct);
                curationInfo.addCurationProduct(curationProductDto);
            }
            curationInfos.add(curationInfo);
        }
        return new PageImpl<>(curationInfos, pageable, totalCount);
    }

    /**
     * Recommend 전체 조회
     * @param pageable
     * @return Recommend
     */
    @Transactional(readOnly = true)
    public Page<CurationDto.CurationInfo> getRecommend(Pageable pageable) {
        List<Curation> curations = curationRepository.findCurations(
            CurationForm.RECOMMEND.getKey(), pageable);
        Long totalCount = curationRepository.countCuration(CurationForm.RECOMMEND.getKey());

        List<CurationDto.CurationInfo> curationInfos = new ArrayList<>();
        for (Curation curation : curations) {
            CurationDto.CurationInfo curationInfo = new CurationInfo(curation);
            List<CurationProduct> curationProductList = curation.getCurationProducts();
            for (CurationProduct curationProduct : curationProductList) {
                CurationDto.CurationProduct curationProductDto = new CurationDto.CurationProduct(
                    curationProduct);
                curationInfo.addCurationProduct(curationProductDto);
            }
            curationInfos.add(curationInfo);
        }
        return new PageImpl<>(curationInfos, pageable, totalCount);
    }

    /**
     * Curation 수정
     * @param updateCuration
     * @param curationId
     * @return 수정된 Curation 내용
     */
    public CurationDto.CurationResponse updateCuration(CurationDto.UpdateCuration updateCuration,
        Long curationId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Curation findCuration = curationRepository.findById(curationId).orElseThrow(
            () -> new Exception(ErrorResult.CURATION_NOT_FOUND));
        Curation curation = findCuration.updateCuration(updateCuration);

        return CurationDto.CurationResponse.of(curationRepository.save(curation));
    }

    /**
     * Curation Image Upload
     * @param multipartFile
     * @param curationId
     * @return s3FileName
     * @throws IOException
     */
    public String uploadImage(MultipartFile multipartFile, Long curationId) throws IOException {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }
        Curation curation = curationRepository.findById(curationId).orElseThrow(
            () -> new Exception(ErrorResult.CURATION_NOT_FOUND));

        InputStream inputStream = multipartFile.getInputStream();
        String originFileName = multipartFile.getOriginalFilename();
        String s3FileName = UUID.randomUUID() + "-" + originFileName;
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(multipartFile.getContentType());
        s3Client.putObject(bucket, s3FileName, inputStream, objMeta);
        curation.uploadImage(s3FileName);
        return s3FileName;
    }

    /**
     * 공개 여부 TRUE
     * @param curationId
     */
    public void display(Long curationId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Curation curation = curationRepository.findById(curationId).orElseThrow(
            () -> new Exception(ErrorResult.CURATION_NOT_FOUND));

        curation.display();
    }

    /**
     * 공개 여부 FALSE
     * @param curationId
     */
    public void unDisplay(Long curationId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new Exception(ErrorResult.ACCESS_PRIVILEGE);
        }

        Curation curation = curationRepository.findById(curationId).orElseThrow(
            () -> new Exception(ErrorResult.CURATION_NOT_FOUND));

        curation.unDisplay();
    }

}
