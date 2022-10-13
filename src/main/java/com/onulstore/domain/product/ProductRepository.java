package com.onulstore.domain.product;

import com.onulstore.domain.enums.DiscountStatus;
import com.onulstore.domain.enums.ProductStatus;
import com.onulstore.web.dto.ProductDto;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);
    
    boolean existsByProductName(String email);
    
    Page<Product> findByBrandId(Long brandId, Pageable pageable);
    
    Optional<Product> findByCategoryId(Long categoryId);

    List<Product> findAllByProductStatusAndDiscountCheck(ProductStatus productStatus,
        boolean discountCheck);

    List<Product> findByProductNameContains(String productName);

    Long countByProductStatusAndCreatedDateAfter(ProductStatus sale, LocalDateTime localDateTime);

    Long countByCreatedDateAfter(LocalDateTime localDateTime);

    boolean existsByDiscountStatus(DiscountStatus discountStatus);

    List<ProductDto.ProductResponse> findAllByCategoryId(Long categoryId);
}