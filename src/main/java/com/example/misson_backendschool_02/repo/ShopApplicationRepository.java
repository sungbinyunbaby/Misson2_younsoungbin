package com.example.misson_backendschool_02.repo;

import com.example.misson_backendschool_02.entity.Shop.ShopApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShopApplicationRepository extends JpaRepository<ShopApplication, Long> {
    @Query("select s from ShopApplication s where s.shop.id = ?1")
    Optional<ShopApplication> findEqualsShopId(Long shopId);
}
