package com.example.misson_backendschool_02.controller;

import com.example.misson_backendschool_02.dto.UserDto;
import com.example.misson_backendschool_02.service.RegularService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comments")
public class RegularUserController { // 일반 사용자가 이용할 수 있는 url
    private final RegularService service;


    // 일반 사용자가 business(사업자 등록번호)를 입력하면 사업자 이용자로 권한 변경.
    @GetMapping("/memberToBusiness")
    public String regularToBusiness(
            @RequestParam("business")Integer business
    ) {
        // 발급 받은 토큰의 username을 추출한다.
        String tokenUser = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("*** business로 권한 변경을 원하는 일반 사용자의 username: {}", tokenUser);

        UserDto updateAuthorization = service.regularToBusiness(
                tokenUser,
                business
        );

        return updateAuthorization.toString();
    }

}
