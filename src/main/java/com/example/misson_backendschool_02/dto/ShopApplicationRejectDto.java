package com.example.misson_backendschool_02.dto;

import com.example.misson_backendschool_02.entity.Shop.Shop;
import com.example.misson_backendschool_02.entity.Shop.ShopApplication;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class ShopApplicationRejectDto {
    // 관리자의 불허
    Long shopApplicationId;
    String shopApplicationApplication;
    String shopApplicationStatus;
    String shopApplicationRejectReason;

    // 쇼핑몰 상태 오픈
    String shopName;
    String shopIntroduce;
    String shopStatus;
    String shopRejectReason;

    public static ShopApplicationRejectDto fromEntity(
            ShopApplication shopApplication,
            Shop shop) {
        return ShopApplicationRejectDto.builder()
                .shopApplicationId(shopApplication.getId())
                .shopApplicationApplication(shopApplication.getApplication())
                .shopApplicationStatus(shopApplication.getStatus())
                .shopApplicationRejectReason(shopApplication.getRejectReason())
                .shopName(shop.getName())
                .shopIntroduce(shop.getIntroduce())
                .shopStatus(shop.getStatus())
                .shopRejectReason(shop.getRejectReason())
                .build();
    }
}
