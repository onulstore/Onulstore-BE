package com.onulstore.domain.user;

import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.enums.Authority;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Users")
@ToString
public class User extends BaseTimeEntity {

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

    @Enumerated(EnumType.STRING)
    private Authority authority;

}
