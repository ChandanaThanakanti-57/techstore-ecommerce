package com.techstore.ecommercemaven.repository;

import com.techstore.ecommercemaven.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository
        extends JpaRepository<Coupon, Long> {

    Coupon findByCode(String code);
}