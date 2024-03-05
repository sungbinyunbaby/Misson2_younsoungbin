package com.example.misson_backendschool_02.controller;

import com.example.misson_backendschool_02.dto.UserDto;
import com.example.misson_backendschool_02.service.ServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ServiceController {
    private final ServiceService service;

    // 권한이 ROEL_INACTIVE인지 확인하고
    // 아니면 ROLE_MEMBER로 업그레이드를 하도록 이동,
    // 맞으면 comment 작성으로 이동.
//    @GetMapping("/comments/checkAuthorization")
//    public String checkAuthorization(UserDto dto) {
//        Boolean authorization = service.createComments(dto);
//        if (authorization) return String.format("/update");
//        return "*** Authorization is MEMBER ***";
//    }
    // 흠,,, 문제는 권한이 inactive라면 권한이 없어서
    // 실행되기 전에 차단이 될 건데,,,
    // 서비스를 하는게 맞을까,,,
    // => 아니다! 이거를 지우고 백엔드로만 생각해서 다시 만들자.
    // 그냥 ChangeToRoleMember를 만들어서 웹접근권한을 hashRole("INACTIVE").

    // 권한이 ROLE_INACTIVE이면 ROLE_MEMBER로 업데이트.
    // 토큰을 기반으로 정보를 알아내야 하니 토큰을 파싱하여 사용자 정보를 추출해보자.
    // => @RequestHeader("Authorization") String token
    @PutMapping("/changeToRoleMember")
    public String ChangeToRoleMember(
//            @RequestHeader("Authorization")String tokenUsername,
            @RequestParam("name")String name,
            @RequestParam("nickname")String nickname,
            @RequestParam("age")Integer age,
            @RequestParam("email")String email,
            @RequestParam("phone")Integer phone
    ) {
        // 발급 받은 토큰의 username을 추출한다.
        String tokenUser = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("***************************** "+tokenUser);

        UserDto updateAuthorization
                = service.changeToRoleMember(
                        tokenUser,
                        name,
                        nickname,
                        age,
                        email,
                        phone);
        return updateAuthorization.toString();
    }


}
