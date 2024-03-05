package com.example.misson_backendschool_02.dto;

import com.example.misson_backendschool_02.entity.Shop.Shop;
import com.example.misson_backendschool_02.entity.Shop.ShopApplication;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class ShopApplicationDto {
    private Long id;
    private String application;
    private String status;
    private Shop shop;
    private  String rejectReason;

    public static ShopApplicationDto fromEntity(ShopApplication entity) {
        return ShopApplicationDto.builder()
                .id(entity.getId())
                .application(entity.getApplication())
                .status(entity.getStatus())
                .shop(entity.getShop())
                .build();
    }
}
