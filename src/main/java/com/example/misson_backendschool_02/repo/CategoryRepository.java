package com.example.misson_backendschool_02.repo;

import com.example.misson_backendschool_02.entity.Shop.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
