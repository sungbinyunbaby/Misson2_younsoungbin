package com.example.misson_backendschool_02.controller;

import com.example.misson_backendschool_02.dto.ApplyShopDto;
import com.example.misson_backendschool_02.dto.ShopApplicationConsentDto;
import com.example.misson_backendschool_02.dto.ShopApplicationDto;
import com.example.misson_backendschool_02.dto.ShopDto;
import com.example.misson_backendschool_02.entity.Shop.Shop;
import com.example.misson_backendschool_02.entity.Shop.ShopApplication;
import com.example.misson_backendschool_02.service.ShopService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;

    // 쇼핑몰의 이름, 소개, 분류가 전부 작성된 상태라면 쇼핑몰을 개설 신청할 수 있다.

    @PostMapping("/createShop")
    public String createShop(
            @RequestBody Shop shop
    ) {
        String tokenUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // 분류의 종류는 서비스 제작자에 의해 미리 정해져 있다.
        ApplyShopDto dto = shopService.createShop(tokenUsername, shop);
        return dto.toString();
    }

    // 관리자는 개설 신청된 쇼핑몰의 목록을 확인할 수 있으며
    @GetMapping("/adminShowApplicationsList")
    public String adminShowApplicationsList() {
        String tokenUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ShopApplicationDto> dtoList = shopService.adminShowShopApplicationsList(tokenUsername);

        return dtoList.toString();
    }

    // 관리자는 정보를 확인 후 허가 또는 불허 할 수 있다.

    // 1. 관리자는 정보를 확인한다.
    @GetMapping("/showShop")
    public String showShop(
            @RequestParam("shopId") Long shopId
    ) {
        String tokenUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        ShopDto dto = shopService.showShop(tokenUsername, shopId);

        return dto.toString();
    }

    // 관리자가 쇼핑몰 개설 허가
    @PostMapping("/shopConsent")
    public String shopConsent(
            @RequestParam("shopId") Long shopId
    ) {
        String tokenUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        String consent = "허가";

        ShopApplicationConsentDto dto = shopService.consent(tokenUsername, shopId, consent);
        return dto.toString();
    }

    // 관리자가 쇼핑몰 개설 불허
    @PostMapping("/shopReject")
    public String shopReject(
            @RequestParam("shopId") Long shopId,
            @RequestBody ShopApplication rejectReason
            ) {
        String tokenUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        String reject = "불허";

        ShopApplicationConsentDto dto =
                shopService.reject(tokenUsername, shopId, reject, rejectReason);
        return dto.toString();
    }
}
