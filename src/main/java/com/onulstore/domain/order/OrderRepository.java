package com.onulstore.domain.order;

import com.onulstore.domain.enums.OrderStatus;
import com.onulstore.domain.member.Member;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o " +
        "where o.member.email = :email " +
        "order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    @Query("select count(o) from Order o " +
        "where o.member.email = :email")
    Long countOrder(@Param("email") String email);

    List<Order> findAllByOrderStatusAndCreatedDateAfter(OrderStatus purchaseConfirm,
        LocalDateTime localDateTime);

    Optional<Order> findByIdAndMember(Long orderId, Member member);

    Long countByOrderStatusAndCreatedDateAfter(OrderStatus orderStatus, LocalDateTime localDateTime);

    Long countByCreatedDateAfter(LocalDateTime localDateTime);

    Long countByOrderStatusAndCreatedDateBetween(OrderStatus orderStatus, LocalDateTime start, LocalDateTime end);
}
