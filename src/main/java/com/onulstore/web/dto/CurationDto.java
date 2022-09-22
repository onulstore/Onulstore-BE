package com.onulstore.web.dto;

import com.onulstore.domain.curation.Curation;
import com.onulstore.domain.product.Product;
import lombok.*;

public class CurationDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CurationRequest {
        private String title;
        private String content;
        private String curationImg;
        private Long productId;
    }

    @Getter
    @Setter
    @Builder
    public static class CurationResponse {
        private Long id;
        private String curationForm;
        private String title;
        private String content;
        private String curationImg;
        private Product product;

        public static CurationResponse of(Curation curation) {
            return CurationResponse.builder()
                    .id(curation.getId())
                    .curationForm(curation.getCurationForm())
                    .title(curation.getTitle())
                    .content(curation.getContent())
                    .curationImg(curation.getCurationImg())
                    .product(curation.getProduct())
                    .build();
        }
    }

}