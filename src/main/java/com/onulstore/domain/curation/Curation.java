package com.onulstore.domain.curation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.enums.CurationForm;
import com.onulstore.domain.member.Member;
import com.onulstore.web.dto.CurationDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
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
    @JsonIgnore
    private Member member;

    @OneToMany(mappedBy = "curation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CurationProduct> curationProducts = new ArrayList<>();

    public void addCurationProduct(CurationProduct curationProduct) {
        curationProducts.add(curationProduct);
        curationProduct.setCuration(this);
    }

    public static Curation createRecommend(String title, String content, Member member,
        CurationProduct curationProduct) {
        Curation curation = new Curation();
        curation.setMember(member);
        curation.addCurationProduct(curationProduct);
        curation.setTitle(title);
        curation.setContent(content);
        curation.setCurationForm(CurationForm.RECOMMEND.getKey());
        return curation;
    }
    
    public Curation updateCuration(CurationDto.UpdateCuration updateCuration) {
        this.title = updateCuration.getTitle();
        this.content = updateCuration.getContent();
        this.curationImg = updateCuration.getCurationImg();
        return this;
    }

    public static Curation createMagazine(String title, String content, Member member,
        List<CurationProduct> curationProducts) {
        Curation curation = new Curation();
        curation.setMember(member);
        for (CurationProduct curationProduct : curationProducts) {
            curation.addCurationProduct(curationProduct);
        }
        curation.setTitle(title);
        curation.setContent(content);
        curation.setCurationForm(CurationForm.MAGAZINE.getKey());
        return curation;
    }

    public void uploadImage(String curationImg) {
        this.curationImg = curationImg;
    }

}
