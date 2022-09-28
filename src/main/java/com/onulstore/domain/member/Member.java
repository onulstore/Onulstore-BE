package com.onulstore.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.cart.Cart;
import com.onulstore.domain.coupon.Coupon;
import com.onulstore.domain.curation.Curation;
import com.onulstore.domain.order.Order;
import com.onulstore.domain.question.Question;
import com.onulstore.domain.questionAnswer.QuestionAnswer;
import com.onulstore.domain.review.Review;
import com.onulstore.domain.wishlist.Wishlist;
import com.onulstore.web.dto.MemberDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String username;

    @Column
    private String phoneNum;

    @Column
    private String roadAddress;

    @Column
    private String buildingName;

    @Column
    private String detailAddress;

    @Column
    private String authority;

    @Column
    @JsonIgnore
    private boolean activated;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<QuestionAnswer> questionAnswers = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Wishlist> wishlists = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Curation> curations = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Coupon> coupons = new ArrayList<>();

    public Member updateProfile(MemberDto.updateRequest updateRequest) {
        this.phoneNum = updateRequest.getPhoneNum();
        this.roadAddress = updateRequest.getRoadAddress();
        this.buildingName = updateRequest.getBuildingName();
        this.detailAddress = updateRequest.getDetailAddress();
        return this;
    }

    public Member updatePassword(String password) {
        this.password = password;
        return this;
    }

}
