package com.example.misson_backendschool_02.entity.Shop;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String application;
    @Setter
    // 개설신청, 허가, 불허
    private String status;
    @Setter
    // 불허 사유
    private String rejectReason;

    @ManyToOne
    private Shop shop;




}
