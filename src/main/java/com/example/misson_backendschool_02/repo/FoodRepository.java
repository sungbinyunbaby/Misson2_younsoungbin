package com.example.misson_backendschool_02.repo;

import com.example.misson_backendschool_02.entity.Shop.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
}
