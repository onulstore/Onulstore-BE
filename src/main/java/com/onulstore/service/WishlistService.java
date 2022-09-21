package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.domain.wishlist.Wishlist;
import com.onulstore.domain.wishlist.WishlistRepository;
import com.onulstore.exception.NotExistUserException;
import com.onulstore.exception.ProductNotFoundException;
import com.onulstore.web.dto.ProductDto;
import com.onulstore.web.dto.WishlistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
                () -> new NotExistUserException("존재하지 않는 유저입니다."));
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new ProductNotFoundException("존재하지 않는 상품입니다."));

        Wishlist wishlist = request.toWishlist(product, member);
        Wishlist findWishlist = wishlistRepository.findByProductIdAndMemberId(product.getId(), member.getId());

        if (findWishlist != null) {
            throw new RuntimeException("이미 찜 등록이 되어있습니다.");
        }
        wishlistRepository.save(wishlist);
        ProductDto.ProductRes productRes = new ProductDto.ProductRes(
                product.getProductName(),
                product.getContent(),
                product.getPrice(),
                product.getProductImg(),
                product.getProductStatus(),
                product.getCategory(),
                productService.isWishlist(member.getId(), product.getId())
        );

        return productRes;
    }

    /**
     * Wishlist 조회
     * @return List<ProductDto.ProductRes> wishlists
     */
    @Transactional(readOnly = true)
    public List<ProductDto.ProductRes> getWishlist() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));

        List<Wishlist> findWishlists = wishlistRepository.findAllByMember(member);
        List<ProductDto.ProductRes> wishlists = new ArrayList<>();

        for (Wishlist wishlist : findWishlists) {
            Product product = wishlist.getProduct();
            ProductDto.ProductRes productRes = new ProductDto.ProductRes(
                    product.getProductName(),
                    product.getContent(),
                    product.getPrice(),
                    product.getProductImg(),
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
                () -> new NotExistUserException("존재하지 않는 유저입니다."));
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("존재하지 않는 상품입니다."));

        Wishlist wishlist = wishlistRepository.findByProductIdAndMemberId(product.getId(), member.getId());
        wishlistRepository.delete(wishlist);

        ProductDto.ProductRes productRes = new ProductDto.ProductRes(
                product.getProductName(),
                product.getContent(),
                product.getPrice(),
                product.getProductImg(),
                product.getProductStatus(),
                product.getCategory(),
                productService.isWishlist(member.getId(), product.getId())
        );
        return productRes;
    }

}
