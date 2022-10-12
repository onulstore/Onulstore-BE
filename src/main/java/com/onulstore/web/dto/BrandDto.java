package com.onulstore.web.dto;

import com.onulstore.domain.brand.Brand;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

public class BrandDto {

    @Getter
    @Setter
    public static class BrandRequest {

        @NotBlank
        @ApiModelProperty(value = "브랜드 이름", required = true)
        private String brandName;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BrandResponse {

        private Long id;
        private String brandName;

        public static BrandResponse of(Brand brand) {
            return BrandResponse.builder()
                .id(brand.getId())
                .brandName(brand.getBrandName())
                .build();
        }

    }

}
