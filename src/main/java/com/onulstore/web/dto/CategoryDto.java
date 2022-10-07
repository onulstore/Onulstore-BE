package com.onulstore.web.dto;

import com.onulstore.domain.category.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class CategoryDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryRequest {

        @ApiModelProperty(value = "카테고리 명", required = true, example = "Category Name")
        @Size(min = 2, max = 30)
        @NotBlank
        private String categoryName;
        @ApiModelProperty(value = "부모 카테고리 아이디", example = "1")
        private Long parentId;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryResponse {

        private Long id;
        private String categoryName;
        private List<CategoryDto> children;

        public static CategoryResponse of(Category category) {
            return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .build();
        }

    }

    @Getter
    @Setter
    @ToString
    public static class updateCatRequest {

        private String categoryName;
    }

}
