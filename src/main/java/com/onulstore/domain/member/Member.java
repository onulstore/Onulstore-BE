package com.onulstore.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onulstore.common.BaseTimeEntity;
import com.onulstore.config.exception.CustomException;
import com.onulstore.domain.cart.Cart;
import com.onulstore.domain.coupon.Coupon;
import com.onulstore.domain.curation.Curation;
import com.onulstore.domain.enums.CustomErrorResult;
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

import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String firstKana;

    @Column
    private String lastKana;

    @Column
    private String username;

    @Column(unique = true)
    private String phoneNum;

    @Column
    private String postalCode;

    @Column
    private String roadAddress;

    @Column
    private String buildingName;

    @Column
    private String detailAddress;

    @Column
    private String authority;

    @Column
    private String provider;

    @Column
    private String providerId;

    @Column
    private int point = 0;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Question> questions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<QuestionAnswer> questionAnswers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cart> carts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Wishlist> wishlists = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Curation> curations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Coupon> coupons = new ArrayList<>();

    public Member updateProfile(MemberDto.UpdateRequest updateRequest) {
        this.firstName = updateRequest.getFirstName();
        this.lastName = updateRequest.getLastName();
        this.firstKana = updateRequest.getFirstKana();
        this.lastKana = updateRequest.getLastKana();
        this.phoneNum = updateRequest.getPhoneNum();
        this.postalCode = updateRequest.getPostalCode();
        this.roadAddress = updateRequest.getRoadAddress();
        this.buildingName = updateRequest.getBuildingName();
        this.detailAddress = updateRequest.getDetailAddress();
        return this;
    }

    public Member updatePassword(String password) {
        this.password = password;
        return this;
    }

    public void deductPoint(Integer mileage) {
        this.setPoint(point - mileage);
        if (point < 0) {
            throw new CustomException(CustomErrorResult.OUT_OF_POINT);
        }
    }

    public void acquirePoint(Integer acquirePoint) {
        this.setPoint(point + acquirePoint);
    }

}
