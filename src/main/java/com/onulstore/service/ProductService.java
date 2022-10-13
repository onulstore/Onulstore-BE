package com.onulstore.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.CustomException;
import com.onulstore.domain.brand.Brand;
import com.onulstore.domain.brand.BrandRepository;
import com.onulstore.domain.category.Category;
import com.onulstore.domain.category.CategoryRepository;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.DiscountStatus;
import com.onulstore.domain.enums.CustomErrorResult;
import com.onulstore.domain.enums.ProductStatus;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductImage;
import com.onulstore.domain.product.ProductImageRepository;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.domain.wishlist.Wishlist;
import com.onulstore.domain.wishlist.WishlistRepository;
import com.onulstore.web.dto.DashboardDto;
import com.onulstore.web.dto.DashboardDto.DashboardProductResponse;
import com.onulstore.web.dto.ProductDto;
import com.onulstore.web.dto.ProductDto.DiscountProductDto;
import com.onulstore.web.dto.ProductDto.ProductResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.dir}")
    private String dir;

    private final AmazonS3Client s3Client;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final WishlistRepository wishlistRepository;
    private final ProductImageRepository productImageRepository;

    private final void authorityCheck(Member member) {
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
            throw new CustomException(CustomErrorResult.ACCESS_PRIVILEGE);
        }
    }

    private final Member getMember() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
    }

    private final void loginCheck() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
    }

    @Transactional
    public ProductDto.ProductResponse register(ProductDto.ProductRequest registration) {
        loginCheck();
        Member member = getMember();
        authorityCheck(member);

        Category category = categoryRepository.findById(registration.getCategoryId()).orElseThrow(
            () -> new CustomException(CustomErrorResult.CATEGORY_NOT_FOUND));
        Brand brand = brandRepository.findById(registration.getBrandId()).orElseThrow(
            () -> new CustomException(CustomErrorResult.BRAND_NOT_FOUND));

        Product product = productRepository.save(registration.toProduct(category, brand));
        product.newPurchaseCount();
        return ProductDto.ProductResponse.of(product);
    }

    @Transactional
    public ProductDto.ProductResponse modify(ProductDto.ProductRequest modification,
        Long productId) {
        loginCheck();
        Member member = getMember();
        authorityCheck(member);
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new CustomException(CustomErrorResult.PRODUCT_NOT_FOUND));

        product.changeProductData(modification.getProductName(),
            modification.getPrice(),
            modification.getQuantity(),
            modification.getProductStatus());
        return ProductDto.ProductResponse.of(productRepository.save(product));
    }

    @Transactional
    public void delete(Long productId) {
        loginCheck();
        Member member = getMember();
        authorityCheck(member);
        Product product = productRepository.findById(productId).orElseThrow(

            () -> new CustomException(CustomErrorResult.ACCESS_PRIVILEGE));
        productRepository.delete(product);
    }

    @Transactional
    public ProductDto.ProductResponse detailInquiry(Long productId, HttpServletRequest request) {
        Member member = new Member();
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            member = getMember();
        }
        HttpSession session = request.getSession();
        boolean check = false;
        ArrayList<Product> latestViewedProductList = (ArrayList) session.getAttribute("List");
        if (latestViewedProductList == null) {
            latestViewedProductList = new ArrayList<>();
        }
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new CustomException(CustomErrorResult.PRODUCT_NOT_FOUND));

        List<Wishlist> wishlists = wishlistRepository.findAllByMember(member);
        for (Wishlist wishlist : wishlists) {
            if (wishlist.getProduct().getId().equals(product.getId())) {
                product.bookmarked();
            }
        }

        for (Product products : latestViewedProductList) {
            if (products.getProductName().equals(product.getProductName())) {
                check = true;
            }
        }
        if (check) {
            latestViewedProductList.remove(product);
        }
        latestViewedProductList.add(product);
        if (latestViewedProductList.size() > 10) {
            latestViewedProductList.remove(0);
        }
        session.setAttribute("List", latestViewedProductList);

        return ProductDto.ProductResponse.of(product);
    }

    @Transactional(readOnly = true)
    public Page entireProductList(Pageable pageable) {
        List<Product> productList = productRepository.findAll();
        List<ProductResponse> productResponseList = new ArrayList<>();
        boolean check = false;
        Member member = new Member();
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            member = getMember();
            check = true;
        }
        for (Product product : productList) {
            if (check) {
                List<Wishlist> wishlists = wishlistRepository.findAllByMember(member);
                for (Wishlist wishlist : wishlists) {
                    if (wishlist.getProduct().getId().equals(product.getId())) {
                        product.bookmarked();
                    }
                }
            }
            productResponseList.add(ProductDto.ProductResponse.of(product));
        }
        return new PageImpl<>(productResponseList, pageable, productResponseList.size());
    }

    @Transactional
    public String uploadContent(MultipartFile multipartFile, Long productId) throws IOException {
        loginCheck();
        Member member = getMember();
        authorityCheck(member);
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new CustomException(CustomErrorResult.PRODUCT_NOT_FOUND));

        InputStream inputStream = multipartFile.getInputStream();
        String originFileName = multipartFile.getOriginalFilename();
        String s3FileName = UUID.randomUUID() + "-" + originFileName;
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(multipartFile.getContentType());
        s3Client.putObject(bucket, s3FileName, inputStream, objMeta);
        product.changeContent(s3FileName);
        return s3FileName;
    }

    @Transactional
    public void uploadImages(List<MultipartFile> multipartFiles, Long productId)
        throws IOException {
        loginCheck();
        Member member = getMember();
        authorityCheck(member);

        Product product = productRepository.findById(productId).orElseThrow(
            () -> new CustomException(CustomErrorResult.PRODUCT_NOT_FOUND));

        for (MultipartFile multipartFile : multipartFiles) {
            ProductImage productImage = new ProductImage();
            InputStream inputStream = multipartFile.getInputStream();

            String originFileName = multipartFile.getOriginalFilename();
            String s3FileName = UUID.randomUUID() + "-" + originFileName;
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentType(multipartFile.getContentType());
            s3Client.putObject(bucket, s3FileName, inputStream, objMeta);

            productImage.setImageName(s3FileName);
            productImage.setProduct(product);
            productImageRepository.save(productImage);
            product.getProductImages().add(productImage);

        }
    }

    /**
     * Wishlist 여부
     * @param memberId
     * @param productId
     */
    @Transactional
    public boolean isWishlist(Long memberId, Long productId) {
        loginCheck();
        Member member = memberRepository.findById(memberId).orElseThrow(
            () -> new CustomException((CustomErrorResult.NOT_EXIST_USER))
        );
        List<Wishlist> wishlists = wishlistRepository.findAllByMember(member);
        for (Wishlist wishlist : wishlists) {
            if (Objects.equals(productId, wishlist.getProduct().getId())) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void deleteImage(Long productId) {
        loginCheck();
        Member member = getMember();
        authorityCheck(member);

        Product product = productRepository.findById(productId).orElseThrow(
            () -> new CustomException(CustomErrorResult.PRODUCT_NOT_FOUND));

        for (ProductImage productImage : product.getProductImages()) {
            productImageRepository.delete(productImage);
        }
        product.getProductImages().clear();
    }

    @Transactional
    public void discountProduct(DiscountProductDto discountDto) {
        loginCheck();
        Member member = getMember();
        authorityCheck(member);
        Product product = productRepository.findById(discountDto.getProductId()).orElseThrow(
            () -> new CustomException(CustomErrorResult.PRODUCT_NOT_FOUND));

        product.discountProduct(discountDto.getDiscountType(), DiscountStatus.TRUE,
            discountDto.getDiscountValue(), discountDto.getDiscountStartDate(),
            discountDto.getDiscountEndDate()
        );
    }

    @Transactional
    public DashboardDto.DashboardProductResponse productDashBoard(LocalDateTime localDateTime) {
        loginCheck();
        Member member = getMember();
        authorityCheck(member);

        Long saleProducts = productRepository.countByProductStatusAndCreatedDateAfter(
            ProductStatus.SALE, localDateTime);
        Long entireProducts = productRepository.countByCreatedDateAfter(
            localDateTime);
        DashboardDto.DashboardProductResponse dashboardProductResponse =
            DashboardProductResponse.builder()
                .onSaleProducts(saleProducts)
                .entireProducts(entireProducts)
                .build();
        return dashboardProductResponse;
    }

    @Transactional
    public Page searchProduct(Pageable pageable, String productName) {
        loginCheck();
        Member member = getMember();
        List<Product> productList = productRepository.findByProductNameContains(productName);
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : productList) {
            List<Wishlist> wishlists = wishlistRepository.findAllByMember(member);
            for (Wishlist wishlist : wishlists) {
                if (wishlist.getProduct().getId().equals(product.getId())) {
                    product.bookmarked();
                }
            }
            productResponseList.add(ProductDto.ProductResponse.of(product));
        }
        return new PageImpl<>(productResponseList, pageable, productResponseList.size());
    }

}
