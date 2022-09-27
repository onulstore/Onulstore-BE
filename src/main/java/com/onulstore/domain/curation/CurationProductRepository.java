package com.onulstore.domain.curation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurationProductRepository extends JpaRepository<CurationProduct, Long> {

    List<CurationProduct> findAllByCuration(Curation curation);

}
