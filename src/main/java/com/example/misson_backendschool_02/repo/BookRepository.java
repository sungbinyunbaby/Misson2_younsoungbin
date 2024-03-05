package com.example.misson_backendschool_02.repo;

import com.example.misson_backendschool_02.entity.Shop.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
