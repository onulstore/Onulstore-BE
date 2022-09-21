package com.onulstore.domain.curation;

import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.enums.CurationForm;
import com.onulstore.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Curation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String curationForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "curation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CurationProduct> curationProducts = new ArrayList<>();

    public void addCurationProduct(CurationProduct curationProduct) {
        curationProducts.add(curationProduct);
        curationProduct.setCuration(this);
    }

    // Magazine 등록
    public static Curation createCurationM(Member member, List<CurationProduct> curationProducts) {
        Curation curationM = new Curation();
        curationM.setMember(member);
        for (CurationProduct curationProduct : curationProducts) {
            curationM.addCurationProduct(curationProduct);
        }
        curationM.setCurationForm(CurationForm.MAGAZINE.getKey());
        return curationM;
    }

    // Recommend 등록
    public static Curation createCurationR(Member member, List<CurationProduct> curationProducts) {
        Curation curationR = new Curation();
        curationR.setMember(member);
        for (CurationProduct curationProduct : curationProducts) {
            curationR.addCurationProduct(curationProduct);
        }
        curationR.setCurationForm(CurationForm.RECOMMEND.getKey());
        return curationR;
    }

}
