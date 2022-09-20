package com.onulstore.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onulstore.config.SecurityUtil;
import com.onulstore.domain.category.Category;
import com.onulstore.domain.category.CategoryRepository;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.exception.AccessPrivilegeExceptions;
import com.onulstore.exception.CategoryNotFoundException;
import com.onulstore.exception.NotExistUserException;
import com.onulstore.web.dto.ProductDto;
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
public class ProductService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.dir}")
    private String dir;

    private final AmazonS3Client s3Client;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductDto.ProductResponse register(ProductDto.ProductRequest registration) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));

        if (!member.getAuthority().equals(Authority.ROLE_ADMIN)) {
            throw new AccessPrivilegeExceptions("접근 권한이 없습니다.");
        }

        Category category = categoryRepository.findById(registration.getCategoryId()).orElseThrow(
                () -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));

        Product product = productRepository.save(
                new Product(registration.getProductName(), registration.getContent(), registration.getPrice(),
                        registration.getQuantity(), registration.getProductImg(), registration.getProductStatus(),
                        category));
        product.newPurchaseCount();
        return ProductDto.ProductResponse.of(product);
    }

    @Transactional
    public ProductDto.ProductResponse modify(ProductDto.modifyRequest modification, Long productId) {

        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));

        if (!member.getAuthority().equals(Authority.ROLE_ADMIN)) {
            throw new AccessPrivilegeExceptions("접근 권한이 없습니다.");
        }

        Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
        product.changeProductData(modification.getProductName(),
            modification.getContent(),
            modification.getPrice(),
            modification.getQuantity(),
            modification.getProductImg(),
            modification.getProductStatus());

        return ProductDto.ProductResponse.of(productRepository.save(product));
    }

    @Transactional
    public void delete(Long productId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));

        if (!member.getAuthority().equals(Authority.ROLE_ADMIN)) {
            throw new AccessPrivilegeExceptions("접근 권한이 없습니다.");
        }
        Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public ProductDto.ProductResponse detailInquiry(Long productId){
        Product product = productRepository.findById(productId).orElseThrow();
        return ProductDto.ProductResponse.of(product);
    }

    @Transactional(readOnly = true)
    public Page entireProductList(Pageable pageable){
        return productRepository.findAll(pageable).map(ProductDto.ProductResponse::of);
    }

    @Transactional
    public String upload(InputStream inputStream, String originFileName) {
        String s3FileName = UUID.randomUUID() + "-" + originFileName;

        ObjectMetadata objMeta = new ObjectMetadata();

        s3Client.putObject(bucket, s3FileName, inputStream, objMeta);

        return s3FileName;

/*        return s3Client.getUrl(bucket, s3FileName).toString()*/
    }

    @Transactional
    public void addImage(Long productId, String image) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));

        if (!member.getAuthority().equals(Authority.ROLE_ADMIN)) {
            throw new AccessPrivilegeExceptions("접근 권한이 없습니다.");
        }
        Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
        product.insertImage(image);
    }
}
