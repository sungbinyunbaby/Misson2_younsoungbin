package com.example.misson_backendschool_02.entity.Shop;

import com.example.misson_backendschool_02.entity.Shop.Shop;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;

}
