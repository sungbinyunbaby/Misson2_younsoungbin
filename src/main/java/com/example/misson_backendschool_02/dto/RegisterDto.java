package com.example.misson_backendschool_02.dto;

import com.example.misson_backendschool_02.entity.RegisterEntity;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class RegisterDto {
    private Long id;
    @Setter
    private String title;
    @Setter
    private String content;
    @Setter
    private Integer minPrice;
    @Setter
    private String status;

    public static RegisterDto fromEntityRegister(RegisterEntity entity, String status) {
        return RegisterDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .minPrice(entity.getMinPrice())
                .status(status)
                .build();
    }
    public static RegisterDto fromEntityRegister(RegisterEntity entity) {
        return RegisterDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .minPrice(entity.getMinPrice())
                .status(entity.getStatus())
                .build();
    }
}
