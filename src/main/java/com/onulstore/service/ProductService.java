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
import com.onulstore.domain.wishlist.Wishlist;
import com.onulstore.domain.wishlist.WishlistRepository;
import com.onulstore.exception.AccessPrivilegeExceptions;
import com.onulstore.exception.CategoryNotFoundException;
import com.onulstore.exception.NotExistUserException;
import com.onulstore.web.dto.ProductDto;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional
  public ProductDto.ProductResponse register(ProductDto.ProductRequest registration) {
    Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
        () -> new NotExistUserException("존재하지 않는 유저입니다."));

    if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
      throw new AccessPrivilegeExceptions("접근 권한이 없습니다.");
    }

    Category category = categoryRepository.findById(registration.getCategoryId()).orElseThrow(
        () -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));

    Product product = productRepository.save(
        new Product(registration.getProductName(), registration.getContent(),
            registration.getPrice(),
            registration.getQuantity(), registration.getProductImg(),
            registration.getProductStatus(),
            category));
    product.newPurchaseCount();
    return ProductDto.ProductResponse.of(product);
  }

  @Transactional
  public ProductDto.ProductResponse modify(ProductDto.modifyRequest modification, Long productId) {

    Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
        () -> new NotExistUserException("존재하지 않는 유저입니다."));

    if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
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

    if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
      throw new AccessPrivilegeExceptions("접근 권한이 없습니다.");
    }
    Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
    productRepository.delete(product);
  }

  @Transactional
  public ProductDto.ProductResponse detailInquiry(Long productId,
      HttpServletRequest request) {

    HttpSession session = request.getSession();
    boolean check = false;
    ArrayList<Product> latestViewedProductList = (ArrayList)session.getAttribute("List");
    if(latestViewedProductList==null){
      latestViewedProductList = new ArrayList<>();
    }
    else{
      for(Product products : latestViewedProductList){
        System.out.println(products.getProductName());
      }
    }

    Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
        () -> new NotExistUserException("존재하지 않는 유저입니다."));

    Product product = productRepository.findById(productId).orElseThrow();

    for(Product products : latestViewedProductList){
      if(products.getProductName().equals(product.getProductName())) {
        check = true;
      }
    }
    if(check==true){
      latestViewedProductList.remove(product);
    }
    latestViewedProductList.add(product);
    if(latestViewedProductList.size()>5){
      latestViewedProductList.remove(0);
    }
    session.setAttribute("List",latestViewedProductList);

    return ProductDto.ProductResponse.of(product);

  }

  @Transactional(readOnly = true)
  public Page entireProductList(Pageable pageable) {
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

    if (!member.getAuthority().equals(Authority.ROLE_ADMIN.getKey())) {
      throw new AccessPrivilegeExceptions("접근 권한이 없습니다.");
    }
    Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
    product.insertImage(image);
  }

  /**
   * Wishlist 여부
   * @param memberId
   * @param productId
   */
  public boolean isWishlist(Long memberId, Long productId) {
    Member member = memberRepository.findById(memberId).orElseThrow(
            () -> new NotExistUserException("존재하지 않는 유저입니다."));
    List<Wishlist> wishlists = wishlistRepository.findAllByMember(member);
    for (Wishlist wishlist : wishlists) {
      if (Objects.equals(productId, wishlist.getProduct().getId())) {
        return true;
      } else {
        continue;
      }
    }
    return false;
  }

}
