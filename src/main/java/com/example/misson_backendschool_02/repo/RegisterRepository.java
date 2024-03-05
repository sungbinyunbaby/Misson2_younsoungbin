package com.example.misson_backendschool_02.repo;

import com.example.misson_backendschool_02.entity.RegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RegisterRepository extends JpaRepository<RegisterEntity, Long> {
}
