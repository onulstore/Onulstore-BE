package com.onulstore.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onulstore.config.SecurityUtil;
import com.onulstore.domain.category.Category;
import com.onulstore.domain.category.CategoryRepository;
import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.UserErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductImage;
import com.onulstore.domain.product.ProductImageRepository;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.domain.wishlist.Wishlist;
import com.onulstore.domain.wishlist.WishlistRepository;
import com.onulstore.exception.UserException;
import com.onulstore.web.dto.ProductDto;
import com.onulstore.web.dto.ProductDto.ProductRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
  private final ProductRepository productRepository;
  private final MemberRepository memberRepository;
  private final CategoryRepository categoryRepository;
  private final WishlistRepository wishlistRepository;
  private final ProductImageRepository productImageRepository;

  @Transactional
  public ProductDto.ProductResponse register(ProductDto.ProductRequest registration) {

    Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
        () -> new UserException(UserErrorResult.NOT_EXIST_USER));

    if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
      throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
    }

    Category category = categoryRepository.findById(registration.getCategoryId()).orElseThrow(
        () -> new UserException(UserErrorResult.CATEGORY_NOT_FOUND));

    Product product = productRepository.save(registration.toProduct(category));
    product.newPurchaseCount();
    return ProductDto.ProductResponse.of(product);
  }

  @Transactional
  public ProductDto.ProductResponse modify(ProductDto.ProductRequest modification, Long productId) {

    Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
        () -> new UserException(UserErrorResult.NOT_EXIST_USER));

    if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
      throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
    }

    Product product = productRepository.findById(productId).orElseThrow(
        () -> new UserException(UserErrorResult.PRODUCT_NOT_FOUND));

    product.changeProductData(modification.getProductName(),
        modification.getContent(),
        modification.getPrice(),
        modification.getQuantity(),
        modification.getProductStatus());

    return ProductDto.ProductResponse.of(productRepository.save(product));
  }

  @Transactional
  public void delete(Long productId) {

    Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
        () -> new UserException(UserErrorResult.NOT_EXIST_USER));

    if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
      throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
    }
    Product product = productRepository.findById(productId).orElseThrow(
        () -> new UserException(UserErrorResult.ACCESS_PRIVILEGE));

    productRepository.delete(product);
  }

  @Transactional
  public ProductDto.ProductResponse detailInquiry(Long productId, HttpServletRequest request) {

    HttpSession session = request.getSession();
    boolean check = false;
    ArrayList<Product> latestViewedProductList = (ArrayList)session.getAttribute("List");
    if(latestViewedProductList == null) {
      latestViewedProductList = new ArrayList<>();
    }
    else{
      for(Product products : latestViewedProductList){
        System.out.println(products.getProductName());
      }
    }


    Product product = productRepository.findById(productId).orElseThrow(
        () -> new UserException(UserErrorResult.PRODUCT_NOT_FOUND));

    for(Product products : latestViewedProductList) {
      if(products.getProductName().equals(product.getProductName())) {
        check = true;
      }
    }
    if(check) {
      latestViewedProductList.remove(product);
    }
    latestViewedProductList.add(product);
    if(latestViewedProductList.size()>10) {
      latestViewedProductList.remove(0);
    }
    session.setAttribute("List",latestViewedProductList);

    return ProductDto.ProductResponse.of(product);

  }

  @Transactional(readOnly = true)
  public Page entireProductList(Pageable pageable) {
    List<Product> productList = productRepository.findAll();
    if(!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser"))
    {
      Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
          .orElseThrow(() -> new UserException(UserErrorResult.NOT_EXIST_USER));

      for (Product product : productList) {
        for (Wishlist wishlist : member.getWishlists()) {
          if (wishlist.getProduct().getId().equals(product.getId())) {
            product.bookmarked();
          }
        }
      }
    }
    int start = (int)pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), productList.size());
    Page<Product> page = new PageImpl<>(productList.subList(start, end), pageable, productList.size());
    return page;
  }

  @Transactional
  public void upload(List<MultipartFile> multipartFiles, Long productId) throws IOException {

    Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
        () -> new UserException(UserErrorResult.NOT_EXIST_USER));

    if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
      throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
    }

    Product product = productRepository.findById(productId).orElseThrow(
        () -> new UserException(UserErrorResult.PRODUCT_NOT_FOUND));

    for(MultipartFile multipartFile : multipartFiles) {
      ProductImage productImage = new ProductImage();
      InputStream inputStream = multipartFile.getInputStream();

      String originFileName = multipartFile.getOriginalFilename();
      String s3FileName = UUID.randomUUID() + "-" + originFileName;
      ObjectMetadata objMeta = new ObjectMetadata();
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

    Member member = memberRepository.findById(memberId).orElseThrow(
        () -> new UserException(UserErrorResult.NOT_EXIST_USER));
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

    Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
        () -> new UserException(UserErrorResult.NOT_EXIST_USER));

    if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
      throw new UserException(UserErrorResult.ACCESS_PRIVILEGE);
    }

    Product product = productRepository.findById(productId).orElseThrow(
        () -> new UserException(UserErrorResult.PRODUCT_NOT_FOUND));

    product.getProductImages().clear();
  }
}
