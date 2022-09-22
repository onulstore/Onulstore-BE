package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.domain.enums.UserErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.domain.wishlist.Wishlist;
import com.onulstore.domain.wishlist.WishlistRepository;
import com.onulstore.exception.UserException;
import com.onulstore.web.dto.ProductDto;
import com.onulstore.web.dto.WishlistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // 찜 등록
    @Transactional
    public WishlistDto.WishlistResponse addWishlist(WishlistDto.WishlistRequest request) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new UserException(UserErrorResult.PRODUCT_NOT_FOUND));

        Wishlist wishlist = request.toWishlist(product,member);

        Wishlist findWishlist = wishlistRepository.findByProductIdAndMemberId(product.getId(), member.getId());

        if (findWishlist != null) {
            throw new UserException(UserErrorResult.WISHLIST_ALREADY_EXIST);
        }
        wishlistRepository.save(wishlist);
        return WishlistDto.WishlistResponse.of(product);

    }

    // 찜 삭제
    @Transactional
    public void deleteWishlist(Long wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }
    
    // 찜 조회
    @Transactional(readOnly = true)
    public List<ProductDto.ProductResponse> getWishlist() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
                () -> new UserException(UserErrorResult.NOT_EXIST_USER));

        return wishlistRepository.findAllByMemberId(member.getId())
                .stream()
                .map(wishlist -> ProductDto.ProductResponse.of(wishlist.getProduct()))
                .collect(Collectors.toList());
    }
}
