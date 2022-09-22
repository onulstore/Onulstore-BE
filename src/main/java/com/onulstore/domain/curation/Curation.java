package com.onulstore.domain.curation;

import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.enums.CurationForm;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.product.Product;
import com.onulstore.web.dto.CurationDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Curation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String curationImg;

    @Column
    private String curationForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public void insertImage(String image) {
        this.curationImg = image;
    }

    public Curation updateCuration(CurationDto.updateCuration updateCuration) {
        this.title = updateCuration.getTitle();
        this.content = updateCuration.getContent();
        this.curationImg = updateCuration.getCurationImg();
        return this;
    }

    public static Curation createCurationM(String title, String content, String curationImg,
                                           Member member, Product product) {
        Curation curation = new Curation();
        curation.setTitle(title);
        curation.setContent(content);
        curation.setCurationImg(curationImg);
        curation.setCurationForm(CurationForm.MAGAZINE.getKey());
        curation.setMember(member);
        curation.setProduct(product);

        return curation;
    }

    public static Curation createCurationR(String title, String content, String curationImg,
                                           Member member, Product product) {
        Curation curation = new Curation();
        curation.setTitle(title);
        curation.setContent(content);
        curation.setCurationImg(curationImg);
        curation.setCurationForm(CurationForm.RECOMMEND.getKey());
        curation.setMember(member);
        curation.setProduct(product);

        return curation;
    }

}
