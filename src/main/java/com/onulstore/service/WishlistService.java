package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.Exception;
import com.onulstore.domain.enums.ErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.domain.wishlist.Wishlist;
import com.onulstore.domain.wishlist.WishlistRepository;
import com.onulstore.web.dto.ProductDto;
import com.onulstore.web.dto.WishlistDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    /**
     * Wishlist 등록
     * @param request
     * @return ProductDto.ProductRes productRes
     */
    public ProductDto.ProductRes addWishlist(WishlistDto.WishlistRequest request) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
            () -> new Exception(ErrorResult.PRODUCT_NOT_FOUND));

        Wishlist wishlist = request.toWishlist(product, member);
        Wishlist findWishlist = wishlistRepository.findByProductIdAndMemberId(product.getId(),
            member.getId());

        if (findWishlist != null) {
            throw new Exception(ErrorResult.WISHLIST_ALREADY_EXIST);
        }
        wishlistRepository.save(wishlist);
        ProductDto.ProductRes productRes = new ProductDto.ProductRes(
            product.getProductName(),
            product.getContent(),
            product.getPrice(),
            product.getProductStatus(),
            product.getCategory(),
            productService.isWishlist(member.getId(), product.getId())
        );
        product.getWishlists().add(wishlist);

        return productRes;
    }

    /**
     * Wishlist 조회
     * @return List<ProductDto.ProductRes> wishlists
     */
    @Transactional(readOnly = true)
    public List<ProductDto.ProductRes> getWishlist() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));

        List<Wishlist> findWishlists = wishlistRepository.findAllByMember(member);
        List<ProductDto.ProductRes> wishlists = new ArrayList<>();

        for (Wishlist wishlist : findWishlists) {
            Product product = wishlist.getProduct();
            ProductDto.ProductRes productRes = new ProductDto.ProductRes(
                product.getProductName(),
                product.getContent(),
                product.getPrice(),
                product.getProductStatus(),
                product.getCategory(),
                productService.isWishlist(member.getId(), product.getId())
            );
            wishlists.add(productRes);
        }
        return wishlists;
    }


    /**
     * Wishlist 삭제
     * @param productId
     * @return ProductDto.ProductRes productRes
     */
    public ProductDto.ProductRes deleteWishlist(Long productId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new Exception(ErrorResult.PRODUCT_NOT_FOUND));

        Wishlist wishlist = wishlistRepository.findByProductIdAndMemberId(product.getId(),
            member.getId());
        wishlistRepository.delete(wishlist);

        ProductDto.ProductRes productRes = new ProductDto.ProductRes(
            product.getProductName(),
            product.getContent(),
            product.getPrice(),
            product.getProductStatus(),
            product.getCategory(),
            productService.isWishlist(member.getId(), product.getId())
        );
        return productRes;
    }

}
