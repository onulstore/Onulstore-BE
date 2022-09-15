package com.onulstore.service;

import com.onulstore.domain.cart.Cart;
import com.onulstore.domain.cart.CartRepository;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.member.MemberRepository;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import com.onulstore.web.dto.CartDto;
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
  public void addCart(CartDto cartDto){
    Member member = memberRepository.findByEmail(cartDto.getMemberEmail()).orElseThrow(RuntimeException::new);
    Product product = productRepository.findById(cartDto.getProductId()).orElseThrow(RuntimeException::new);
    if(product.getQuantity() < cartDto.getQuantity()){
      throw new RuntimeException();
    }
    Cart cart = Cart.builder()
        .member(member)
        .product(product)
        .productCount(cartDto.getQuantity())
        .build();

    member.getCarts().add(cart);
    product.getCarts().add(cart);
    cartRepository.save(cart);
  }

  public void deleteCart(Long cartId) {
    Cart cart = cartRepository.findById(cartId).orElseThrow(RuntimeException::new);
    cartRepository.deleteById(cartId);
  }

  public CartDto shoppingCart(Long cartId) {
    Cart cart = cartRepository.findById(cartId).orElseThrow(RuntimeException::new);
    CartDto cartDto = CartDto.builder()
        .memberEmail(cart.getMember().getEmail())
        .productId(cart.getProduct().getId())
        .quantity(cart.getProductCount())
        .build();
    return cartDto;
  }
}
