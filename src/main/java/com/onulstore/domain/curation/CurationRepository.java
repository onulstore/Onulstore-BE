package com.onulstore.domain.curation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CurationRepository extends JpaRepository<Curation, Long> {

    @Query("select o from Curation o " +
            "where o.member.email = :email " +
            "order by o.createdDate desc")
    List<Curation> findCurations(@Param("email") String email, Pageable pageable);

    @Query("select count(o) from Curation o " +
            "where o.member.email = :email")
    Long countCuration(@Param("email") String email);

}
