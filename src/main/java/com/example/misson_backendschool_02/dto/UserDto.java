package com.example.misson_backendschool_02.dto;

import com.example.misson_backendschool_02.entity.User;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private Long id;
    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private String role;

    @Setter
    private String name;
    @Setter
    private String nickname;
    @Setter
    private Integer age;
    @Setter
    private String email;
    @Setter
    private Integer phone;
    @Setter
    private Integer business;

    public static UserDto fromEntity(User entity, String role) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .name(entity.getName())
                .role(role)
                .build();
    }

    public static UserDto fromEntityMember(User entity, String role) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .name(entity.getName())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .age(entity.getAge())
                .phone(entity.getPhone())
                .role(role)
                .build();
    }

    public static UserDto fromEntityBusiness(User entity, String role, Integer business) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .name(entity.getName())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .age(entity.getAge())
                .phone(entity.getPhone())
                .role(role)
                .business(entity.getBusiness())
                .build();
    }

}
