package com.onulstore.domain.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.enums.AnswerStatus;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.questionAnswer.QuestionAnswer;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column
    private String answerState;    // 답변 유무

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<QuestionAnswer> questionAnswers = new ArrayList<>();

    public void unAnswered() {
        this.answerState = AnswerStatus.NO.getKey();
    }

    public void Answered() {
        this.answerState = AnswerStatus.YES.getKey();
    }

}
