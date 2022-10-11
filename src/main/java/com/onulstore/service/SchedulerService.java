package com.onulstore.service;

import com.onulstore.domain.coupon.Coupon;
import com.onulstore.domain.coupon.CouponRepository;
import com.onulstore.domain.enums.DiscountStatus;
import com.onulstore.domain.enums.DiscountType;
import com.onulstore.domain.enums.ProductStatus;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void checkExpiration(){
        if(!productRepository.existsByDiscountStatus(DiscountStatus.ONUL)){
            todayDiscount();
        }
        discountProductValidation();
        discountCouponValidation();
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void todayDiscount() {
        List<Product> productList = productRepository.findAllByProductStatusAndDiscountCheck(
            ProductStatus.SALE, false);
        for (int i = 0; i < 6; i++) {
            int random = new Random().nextInt(productList.size());
            if(productList.get(random).getDiscountValue() > 0){
                i = i-1;
                continue;
            }
            Product product = productList.get(random);
            product.discountProduct(DiscountType.PERCENT, DiscountStatus.ONUL, 5, LocalDate.now(), LocalDate.now());
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void discountProductValidation() {
        List<Product> productList = productRepository.findAll();
        for (Product product : productList) {
            product.discountStartValidation();
            product.discountEndValidation();
        }
    }

    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void discountCouponValidation() {
        List<Coupon> couponList = couponRepository.findAll();
        for (Coupon coupon : couponList) {
            coupon.checkValidation();
        }
    }
}
