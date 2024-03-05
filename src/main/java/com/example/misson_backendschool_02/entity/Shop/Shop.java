package com.example.misson_backendschool_02.entity.Shop;

import com.example.misson_backendschool_02.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String status;
    @Setter
    private String rejectReason;
    @Setter
    private String name;
    @Setter
    private String introduce;

    @Setter
    @ManyToOne
    private Category category;
    @OneToOne
    private User user;


}
