package com.example.misson_backendschool_02.repo;

import com.example.misson_backendschool_02.entity.Shop.Health;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthRepository extends JpaRepository<Health, Long> {
}
