package com.example.misson_backendschool_02.repo;

import com.example.misson_backendschool_02.entity.Shop.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
}
