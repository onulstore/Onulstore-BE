package com.onulstore.service;

import com.onulstore.config.SecurityUtil;
import com.onulstore.config.exception.Exception;
import com.onulstore.domain.cart.Cart;
import com.onulstore.domain.cart.CartRepository;
import com.onulstore.domain.enums.ErrorResult;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.web.dto.CartDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addCart(CartDto cartDto) {
        Member member = memberRepository.findByEmail(cartDto.getMemberEmail()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));
        Product product = productRepository.findById(cartDto.getProductId()).orElseThrow(
            () -> new Exception(ErrorResult.PRODUCT_NOT_FOUND));
        boolean duplicate = false;

        if (product.getQuantity() < cartDto.getQuantity()) {
            throw new Exception(ErrorResult.OUT_OF_STOCK);
        }

        for (Cart carts : member.getCarts()) {
            if (carts.getProduct().getProductName().equals(product.getProductName())) {
                carts.changeQuantity(cartDto.getQuantity());
                duplicate = true;
            }
        }

        if (!duplicate) {
            Cart cart = Cart.builder()
                .member(member)
                .product(product)
                .productCount(cartDto.getQuantity())
                .build();

            member.getCarts().add(cart);
            product.getCarts().add(cart);
            cartRepository.save(cart);
        }
    }

    @Transactional
    public void deleteCart(Long cartId) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));

        Cart cart = cartRepository.findById(cartId).orElseThrow(RuntimeException::new);
        cartRepository.delete(cart);
        member.getCarts().remove(cart);
    }

    @Transactional
    public List<CartDto> getCartList() {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(
            () -> new Exception(ErrorResult.NOT_EXIST_USER));

        List<CartDto> cartDtoList = new ArrayList<CartDto>();
        for (Cart cart : member.getCarts()) {
            CartDto cartDto = CartDto.of(cart);
            cartDtoList.add(cartDto);
        }
        return cartDtoList;
    }

    @Transactional
    public CartDto plusOne(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
            () -> new Exception(ErrorResult.CART_NOT_FOUND));

        cart.plusOne();
        CartDto cartDto = CartDto.of(cart);
        return cartDto;
    }

    @Transactional
    public CartDto minusOne(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
            () -> new Exception(ErrorResult.CART_NOT_FOUND));

        cart.minusOne();
        CartDto cartDto = CartDto.of(cart);
        return cartDto;
    }
}
