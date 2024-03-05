package com.example.misson_backendschool_02.entity;

import com.example.misson_backendschool_02.dto.UserDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class User {
    // id, password, username, nickName, age, email, phone
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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



}
