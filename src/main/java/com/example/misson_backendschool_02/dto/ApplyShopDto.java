package com.example.misson_backendschool_02.dto;

import com.example.misson_backendschool_02.entity.Shop.Shop;
import com.example.misson_backendschool_02.entity.Shop.ShopApplication;
import lombok.*;

// 쇼핑몰을 개설 신청한 Shop 데이터와 데어터가 저장된 ShopApplication을 보여준다.
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class ApplyShopDto {
    private Long shopId;
    private String shopName;
    private String shopIntroduce;

    private Long shopApplicationId;
    private String shopApplicationStatus;
    private Long shopApplicationShopId;

    private Long categoryId;
    private String categoryName;
}
