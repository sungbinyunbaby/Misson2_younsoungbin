package com.example.misson_backendschool_02.service;

import com.example.misson_backendschool_02.dto.UserDto;
import com.example.misson_backendschool_02.entity.User;
import com.example.misson_backendschool_02.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegularService {
    private final UserRepository repository;
    private final ShopService shopService;

    // business로 권한을 변경하는 메서드
    public UserDto regularToBusiness(String tokenUsername, Integer business) {
        Optional<User> username = repository.findByUsername(tokenUsername);
        User usernameInToken = username.orElseThrow(()-> new UsernameNotFoundException(tokenUsername));
        log.info("*** 권한을 business로 만들고 싶은 username: {} ***", usernameInToken.getUsername());

        String role = "ROLE_BUSINESS";

        usernameInToken.setRole(role);
        usernameInToken.setBusiness(business);

       log.info(usernameInToken.getUsername());
       log.info(usernameInToken.getRole());

       repository.save(usernameInToken);
       log.info("*** " + usernameInToken.getUsername() + "님의 권한은 " + role + "이 되었습니다.");

       // 일반 사용자가 사업자 사용자로 전환될 때 준비중 상태의 쇼핑몰이 추가된다.
        // 사업자 사용자는 이 쇼핑몰의 주인이 된다.
        shopService.shopStatusPreparing(usernameInToken);

       return UserDto.fromEntityBusiness(usernameInToken, role, business);


    }
}
