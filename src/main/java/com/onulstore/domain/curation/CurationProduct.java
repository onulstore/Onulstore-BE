package com.onulstore.domain.curation;

import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.product.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CurationProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String curationImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curation_id")
    private Curation curation;

    public static CurationProduct createCurationProduct(String title, String content,
                                                        String curationImg, Product product) {
        CurationProduct curationProduct = new CurationProduct();
        curationProduct.setTitle(title);
        curationProduct.setContent(content);
        curationProduct.setCurationImg(curationImg);
        curationProduct.setProduct(product);

        return curationProduct;
    }

}
