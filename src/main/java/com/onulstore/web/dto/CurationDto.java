package com.onulstore.web.dto;

import com.onulstore.domain.curation.Curation;
import com.onulstore.domain.enums.ProductStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class CurationDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RecommendRequest {

        private Long productId;
        private String title;
        private String content;
    }

    @Getter
    @Setter
    public static class CurationInfo {

        private Long curationId;
        private String title;
        private String content;
        private String curationImg;
        private String curationForm;
        private boolean display;
        private LocalDateTime createdDate;
        private List<CurationProduct> curationProducts = new ArrayList<>();

        public CurationInfo(Curation curation) {
            this.curationId = curation.getId();
            this.title = curation.getTitle();
            this.content = curation.getContent();
            this.curationImg = curation.getCurationImg();
            this.curationForm = curation.getCurationForm();
            this.display = curation.isDisplay();
            this.createdDate = curation.getCreatedDate();
        }

        public void addCurationProduct(CurationDto.CurationProduct curationProduct) {
            curationProducts.add(curationProduct);
        }

    }

    @Getter
    @Setter
    public static class CurationProduct {

        private String productName;
        private String brandName;
        private Integer price;
        private Integer discountValue;
        private ProductStatus productStatus;
        private float rating;

        public CurationProduct(com.onulstore.domain.curation.CurationProduct curationProduct) {
            this.productName = curationProduct.getProduct().getProductName();
            this.brandName = curationProduct.getProduct().getBrand().getBrandName();
            this.price = curationProduct.getProduct().getPrice();
            this.discountValue = curationProduct.getProduct().getDiscountValue();
            this.productStatus = curationProduct.getProduct().getProductStatus();
            this.rating = curationProduct.getProduct().getRating();
        }
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MagazineRequest {

        private List<Long> productList = new ArrayList<>();
        private String title;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AddProductRequest {

        private Long productId;
        private Long curationId;
    }

    @Getter
    @Setter
    @Builder
    public static class CurationResponse {

        private Long id;
        private String title;
        private String content;
        private String curationImg;
        private String curationForm;
        private boolean display;

        public static CurationResponse of(Curation curation) {
            return CurationResponse.builder()
                    .id(curation.getId())
                    .curationForm(curation.getCurationForm())
                    .title(curation.getTitle())
                    .content(curation.getContent())
                    .curationImg(curation.getCurationImg())
                    .display(curation.isDisplay())
                    .build();
        }
    }

    @Getter
    @Setter
    @ToString
    public static class UpdateCuration {

        private String title;
        private String content;
    }

}