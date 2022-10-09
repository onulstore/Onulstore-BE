package com.onulstore.domain.payment;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    boolean existsByOrderId(Long orderId);

    @Query("select o from Payment o " +
        "where o.order.id = :id " +
        "order by o.createdDate desc")
    List<Payment> findPaymentsByOrderId(@Param("id") Long id, Pageable pageable);

}
