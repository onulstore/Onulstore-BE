package com.onulstore.domain.brand;

import com.onulstore.web.dto.BrandDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String brandName;

    public Brand(String brandName) {
        this.brandName = brandName;
    }

    public Brand updateBrand(BrandDto.UpdateRequest updateRequest) {
        this.brandName = updateRequest.getBrandName();
        return this;
    }

}
