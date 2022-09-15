package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.domain.wishlist.Wishlist;
import com.onulstore.domain.wishlist.WishlistRepository;
import com.onulstore.exception.NotExistUserException;
import com.onulstore.web.dto.WishlistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // 찜 등록
    public WishlistDto.WishlistResponse addWishlist(WishlistDto.WishlistRequest request) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 유저입니다."));
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new NotExistUserException("존재하지 않는 상품입니다."));

        Wishlist wishlist = request.toWishlist(product,member);

        Wishlist findWishlist = wishlistRepository.findByProductIdAndMemberId(product.getId(), member.getId());

        if (findWishlist != null) {
            throw new RuntimeException("이미 찜 등록이 되어있습니다.");
        }
        wishlistRepository.save(wishlist);
        return WishlistDto.WishlistResponse.of(product);

    }

    // 찜 삭제
    @Transactional
    public void deleteWishlist(Long wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }


}
