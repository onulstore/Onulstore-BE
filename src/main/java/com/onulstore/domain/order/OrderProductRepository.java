package com.onulstore.domain.order;

import com.onulstore.domain.product.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findAllByOrderId(Long orderId);

    Optional<OrderProduct> findOrderByProduct(Product product);

}
