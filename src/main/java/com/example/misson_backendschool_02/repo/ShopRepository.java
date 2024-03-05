package com.example.misson_backendschool_02.repo;

import com.example.misson_backendschool_02.entity.Shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Query("select s from Shop s where s.user.username = ?1")
    Optional<Shop> findByUsernameStatusReady(String username);
}
