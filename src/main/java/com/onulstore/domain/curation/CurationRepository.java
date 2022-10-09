package com.onulstore.domain.curation;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CurationRepository extends JpaRepository<Curation, Long> {

    @Query("select o from Curation o " +
        "where o.curationForm = :curationForm " +
        "order by o.updatedDate desc")
    List<Curation> findCurations(@Param("curationForm") String curationForm, Pageable pageable);

    @Query("select count(o) from Curation o " +
        "where o.curationForm = :curationForm")
    Long countCuration(@Param("curationForm") String curationForm);

}
