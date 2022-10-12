package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.CustomException;
import com.onulstore.domain.cart.Cart;
import com.onulstore.domain.cart.CartRepository;
import com.onulstore.domain.enums.CustomErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.web.dto.CartDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addCart(CartDto.CartRequest cartRequest) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(cartRequest.getProductId()).orElseThrow(
            () -> new CustomException(CustomErrorResult.PRODUCT_NOT_FOUND));
        boolean duplicate = false;

        if (product.getQuantity() < cartRequest.getQuantity()) {
            throw new CustomException(CustomErrorResult.OUT_OF_STOCK);
        }

        for (Cart carts : member.getCarts()) {
            if (carts.getProduct().getProductName().equals(product.getProductName())) {
                carts.changeQuantity(cartRequest.getQuantity());
                duplicate = true;
            }
        }

        if (!duplicate) {
            Cart cart = Cart.builder()
                .member(member)
                .product(product)
                .productCount(cartRequest.getQuantity())
                .build();

            member.getCarts().add(cart);
            product.getCarts().add(cart);
            cartRepository.save(cart);
        }
    }

    @Transactional
    public void deleteCart(Long cartId) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CustomException(
            CustomErrorResult.CART_NOT_FOUND));
        
        cartRepository.delete(cart);
        member.getCarts().remove(cart);
    }

    @Transactional
    public List<CartDto.CartResponse> getCartList() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));

        List<CartDto.CartResponse> cartResponseList = new ArrayList<>();
        for (Cart cart : member.getCarts()) {
            CartDto.CartResponse cartResponse = CartDto.CartResponse.of(cart);
            cartResponseList.add(cartResponse);
        }
        return cartResponseList;
    }

    @Transactional
    public CartDto.CartResponse plusOne(Long cartId) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        Cart cart = cartRepository.findById(cartId).orElseThrow(
            () -> new CustomException(CustomErrorResult.CART_NOT_FOUND));

        cart.plusOne();
        CartDto.CartResponse cartResponse = CartDto.CartResponse.of(cart);
        return cartResponse;
    }

    @Transactional
    public CartDto.CartResponse minusOne(Long cartId) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            .equals("anonymousUser")) {
            throw new CustomException(CustomErrorResult.LOGIN_NEEDED);
        }
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow(() -> new CustomException(CustomErrorResult.NOT_EXIST_USER));
        Cart cart = cartRepository.findById(cartId).orElseThrow(
            () -> new CustomException(CustomErrorResult.CART_NOT_FOUND));

        cart.minusOne();
        CartDto.CartResponse cartResponse = CartDto.CartResponse.of(cart);
        return cartResponse;
    }
}
