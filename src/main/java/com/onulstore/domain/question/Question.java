package com.onulstore.domain.question;

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
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;
    
    private boolean answerState;    // 답변 유무

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
