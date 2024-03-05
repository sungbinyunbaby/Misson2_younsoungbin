package com.example.misson_backendschool_02.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 제목, 설명, 최소가격(가격 설정), (대표 이미지 - 미정, 반드시 포함될 필요없다 - 대표이미지 제외한 dto도 만들자.)
    @Setter
    private String title;
    @Setter
    private String content;
    @Setter
    private Integer minPrice;
    @Setter
    private String status;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
