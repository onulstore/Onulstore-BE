package com.onulstore.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
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

    @Transactional
    public ProductDto.ProductResponse register(ProductDto.ProductRequest registration) {
        if(productRepository.existsByProductName(registration.getProductName())) {
            throw new RuntimeException("이미 존재하는 상품입니다.");
        }
        Product product = registration.toProduct();
        product.newPurchaseCount();
        return ProductDto.ProductResponse.of(productRepository.save(product));
    }

    @Transactional
    public ProductDto.ProductResponse modify(ProductDto.ProductRequest modification, Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
        product.changeProductData(modification.getProductName(),
            modification.getContent(),
            modification.getLargeCategoryCode(),
            modification.getSmallCategoryCode(),
            modification.getPrice(),
            modification.getQuantity(),
            modification.getProductImg(),
            modification.getProductStatus());

        return ProductDto.ProductResponse.of(productRepository.save(product));
    }

    @Transactional
    public void delete(Long productId) {
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
        Page<ProductDto.ProductResponse> pages = productRepository.findAll(pageable).map(product -> ProductDto.ProductResponse.of(product));
        return pages;
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
        Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
        product.insertImage(image);
    }
}
