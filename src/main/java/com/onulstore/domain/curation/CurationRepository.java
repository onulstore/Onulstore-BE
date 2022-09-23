package com.onulstore.domain.curation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurationRepository extends JpaRepository<Curation, Long> {

    Page<Curation> findAllByCurationForm(String curationForm, Pageable pageable);

}
