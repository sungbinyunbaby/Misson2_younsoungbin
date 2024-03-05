package com.example.misson_backendschool_02.controller;

import com.example.misson_backendschool_02.dto.JoinDto;
import com.example.misson_backendschool_02.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {
    private final JoinService service;

    @GetMapping("/create/inactive")
    public String createInactive(
            @RequestBody JoinDto joinDto
    ) {
        service.joinProcess(joinDto);
        return "비활성 회원가입 완료(첫번째 회원가입)";
    }

    @GetMapping("/comments")
    public String comment() {
        return "Authorized";
    }

    // 회원가입이 제대로 이루어졌다면
    // 권한이 inactive일테니 접근가능.
    @GetMapping("/authorization/inactive")
    public String inactive() {
        return "Inactive";
    }
}
