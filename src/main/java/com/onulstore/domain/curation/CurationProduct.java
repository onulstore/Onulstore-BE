package com.onulstore.domain.curation;

import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.product.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class CurationProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curation_id")
    private Curation curation;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    public static CurationProduct createCurationProduct(Product product) {
        CurationProduct curationProduct = new CurationProduct();
        curationProduct.setProduct(product);
        return curationProduct;
    }

    public static CurationProduct addProductMagazine(Curation curation, Product product) {
        CurationProduct curationProduct = new CurationProduct();
        curationProduct.setCuration(curation);
        curationProduct.setProduct(product);
        return curationProduct;
    }

}
