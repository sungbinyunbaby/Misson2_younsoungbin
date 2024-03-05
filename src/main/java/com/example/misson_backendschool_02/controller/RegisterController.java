package com.example.misson_backendschool_02.controller;

import com.example.misson_backendschool_02.dto.RegisterDto;
import com.example.misson_backendschool_02.service.RegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/register") // 물품 등록.
public class RegisterController {
    private final RegisterService service;


    // 게시글 만들기
    @PostMapping("/addItem") // member 권한 이상부터 게시글 만들기 가능.
    public String addRegisterItem(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("minPrice") Integer minPrice
    ) {
        String token = SecurityContextHolder.getContext().getAuthentication().getName();

        RegisterDto dto = service.addRegisterItem(token, title, content, minPrice);
        return dto.toString();
    }

//    @GetMapping("/boards/{registerId}")
    @GetMapping("/boards")
    public String registerBoards(
//            @PathVariable Long registerId
            @RequestParam("registerId") Long registerId
    ) {

        log.info("*** 게시판 조회를 시작하겠습니다. ***");
        String token = SecurityContextHolder.getContext().getAuthentication().getName();

        RegisterDto dto = service.registerBoards(registerId, token);

        return dto.toString();
    }

     // 등록된 물품 정보는 작성자가 수정, 삭제 가능하다.
    @PostMapping("/updateCheck")
    public String updateCheck(
            @RequestParam("registerId") Long id

    ) {
        String token = SecurityContextHolder.getContext().getAuthentication().getName();
        Boolean checkUser = service.updateCheck(token, id);
        if (checkUser){
            return "수정가능합니다.";
        }
        return "게시글의 작성자가 아니므로 계시글 수정이 불가능합니다.";
    }

    // Update
    @PutMapping("/updateItem")
    public String updateItem(
            @RequestParam("registerId") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("minPrice") Integer minPrice
    ) {
        String token = SecurityContextHolder.getContext().getAuthentication().getName();
        RegisterDto dto = service.updateItem(token, id, title, content, minPrice);
        return dto.toString();
    }

    // Delete
    @DeleteMapping("/deleteItem")
    public String deleteItem(
            @RequestParam("registerId") Long id
    ) {
        String token = SecurityContextHolder.getContext().getAuthentication().getName();
       service.deleteItem(token, id);
        return "해당 게시글이 삭제되었습니다.";
    }
}
