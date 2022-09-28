package com.onulstore.web.dto;

import com.onulstore.domain.curation.Curation;
import lombok.*;

public class CurationDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RecommendRequest {
        private String title;
        private String content;
        private String curationImg;
        private Long productId;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MagazineRequest {
        private String title;
        private String content;
        private String curationImg;
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

        public static CurationResponse of(Curation curation) {
            return CurationResponse.builder()
                    .id(curation.getId())
                    .curationForm(curation.getCurationForm())
                    .title(curation.getTitle())
                    .content(curation.getContent())
                    .curationImg(curation.getCurationImg())
                    .build();
        }
    }

    @Getter
    @Setter
    @ToString
    public static class UpdateCuration {
        private String title;
        private String content;
        private String curationImg;
    }

}