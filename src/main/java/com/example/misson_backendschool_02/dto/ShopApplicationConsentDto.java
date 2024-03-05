package com.example.misson_backendschool_02.dto;

import com.example.misson_backendschool_02.entity.Shop.Shop;
import com.example.misson_backendschool_02.entity.Shop.ShopApplication;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class ShopApplicationConsentDto {
    // 관리자의 허가
    Long shopApplicationId;
    String shopApplicationApplication;
    String shopApplicationStatus;

    // 쇼핑몰 상태 오픈
    String shopName;
    String shopIntroduce;
    String shopStatus;

    public static ShopApplicationConsentDto fromEntity(
            ShopApplication shopApplication,
            Shop shop) {
        return ShopApplicationConsentDto.builder()
                .shopApplicationId(shopApplication.getId())
                .shopApplicationApplication(shopApplication.getApplication())
                .shopApplicationStatus(shopApplication.getStatus())
                .shopName(shop.getName())
                .shopIntroduce(shop.getIntroduce())
                .shopStatus(shop.getStatus())
                .build();
    }
}
