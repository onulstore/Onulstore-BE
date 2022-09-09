package com.onulstore.domain.review;

import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private float rate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
