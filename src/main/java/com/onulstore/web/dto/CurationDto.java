package com.onulstore.web.dto;

import com.onulstore.domain.curation.Curation;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    public static class CurationResponse {
        private Long curationId;
        private String curationForm;
        private List<CurationProduct> curationProducts = new ArrayList<>();

        public CurationResponse(Curation curation) {
            this.curationId = curation.getId();
            this.curationForm = curation.getCurationForm();
        }

        public void addCurationProduct(CurationDto.CurationProduct curationDetails) {
            curationProducts.add(curationDetails);
        }
    }

    @Getter
    @Setter
    public static class CurationProduct {
        private String title;
        private String content;
        private String curationImg;

        public CurationProduct(com.onulstore.domain.curation.CurationProduct curationProduct) {
            this.title = curationProduct.getTitle();
            this.content = curationProduct.getContent();
            this.curationImg = curationProduct.getCurationImg();
        }
    }
}