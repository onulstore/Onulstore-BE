package com.onulstore.domain.questionAnswer;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {

    Optional<QuestionAnswer> findByQuestionId(Long questionId);
}
