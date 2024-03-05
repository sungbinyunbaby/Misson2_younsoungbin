package com.example.misson_backendschool_02.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class ShopDto {
    private Long id;
    private String name;
    private String introduce;
    private String status;
    private String rejectReason;
}
