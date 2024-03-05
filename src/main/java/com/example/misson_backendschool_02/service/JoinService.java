package com.example.misson_backendschool_02.service;

import com.example.misson_backendschool_02.dto.JoinDto;
import com.example.misson_backendschool_02.dto.UserDto;
import com.example.misson_backendschool_02.entity.User;
import com.example.misson_backendschool_02.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JoinService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository repository;

    public void joinProcess(JoinDto joinDto) {
        String username = joinDto.getUsername();
        String password = joinDto.getPassword();
        String role = "ROLE_INACTIVE";

        Boolean isExist = repository.existsByUsername(username);

        if (isExist) return;

        User user = User.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .role(role)
                .build();
        log.info("\n userid: {} \n userPassword: {} \n userRole: {}"
                ,user.getUsername(), user.getPassword(), user.getRole());


        user = repository.save(user);

        UserDto.fromEntity(user, role);

    }
}
