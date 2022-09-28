package com.onulstore.web.dto;

import com.onulstore.domain.category.Category;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductImage;
import com.onulstore.domain.wishlist.Wishlist;
import com.onulstore.web.dto.ProductDto.ProductResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class ProductImageDto {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class ProductImageMaker {

    private String imageName;

    public ProductImage toProductImage() {
      return ProductImage.builder()
          .imageName(imageName)
          .build();
    }

    public static List<ProductImageMaker> of(List<ProductImage> productImageList){
      List<ProductImageMaker> productImageDtoList = new ArrayList<>();
      for(ProductImage productImage : productImageList){
        productImageDtoList.add(ProductImageMaker.builder()
            .imageName(productImage.getImageName())
            .build());
      }
      return productImageDtoList;

    }

  }

}
