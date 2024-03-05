package com.example.misson_backendschool_02.service;

import com.example.misson_backendschool_02.dto.ApplyShopDto;
import com.example.misson_backendschool_02.dto.ShopApplicationConsentDto;
import com.example.misson_backendschool_02.dto.ShopApplicationDto;
import com.example.misson_backendschool_02.dto.ShopDto;
import com.example.misson_backendschool_02.entity.Shop.Category;
import com.example.misson_backendschool_02.entity.Shop.Shop;
import com.example.misson_backendschool_02.entity.Shop.ShopApplication;
import com.example.misson_backendschool_02.entity.User;
import com.example.misson_backendschool_02.repo.CategoryRepository;
import com.example.misson_backendschool_02.repo.ShopApplicationRepository;
import com.example.misson_backendschool_02.repo.ShopRepository;
import com.example.misson_backendschool_02.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopService {
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final ShopApplicationRepository shopApplicationRepository;
    private final CategoryRepository categoryRepository;

    // 1.
    // 일반 사용자가 사업자 사용자로 전환될 때 준비중 상태의 쇼핑몰이 추가된다.
    // 사업자 사용자는 이 쇼핑몰의 주인이 된다.
    public void shopStatusPreparing(User user) {

        // 유저의 권한이 비지니스이면 Shop.status를 준비중으로 바꾼다.
        if (user.getRole().equals("ROLE_BUSINESS")) {
            String status = "준비 중";
            Shop shop = Shop.builder()
                    .status(status)
                    .user(user)
                    .build();
            shopRepository.save(shop);
            log.info("*** 준비 중 상태의 쇼핑몰이 추가 되었습니다. ***");
        }
    }

    // 2.
    // 쇼핑몰의 이름, 소개, 분류가 전부 작성된 상태라면 쇼핑몰을 개설 신청할 수 있다.
    public ApplyShopDto createShop(
            String tokenUsername,
            Shop shopBody
    ) {
        // 쇼핑몰 등록자가 존재하는지 확인.(orElse())
        User user = tokenUsername(tokenUsername);
        // 사용자의 쇼핑몰 등록을 확인. 이걸 사용해서 사용자의 쇼핑몰 설정.
        Optional<Shop> shopUser = shopRepository.findByUsernameStatusReady(user.getUsername());
        Shop shop = shopUser.orElseThrow(()-> new UsernameNotFoundException(user.getUsername()));

        // 분류의 종류는 서비스 제작자에 의해 미리 정해져 있다.
        Category category = Category.builder()
                .name(user.getName() + "님의 " + shopBody.getName()+" 쇼핑몰 카테고리")
                .build();
        categoryRepository.save(category);
        // 쇼핑몰을 준비 중인 user라면 자신의 쇼핑몰의 이름과 소개를 작성한다.
        shop.setName(shopBody.getName());
        shop.setIntroduce(shopBody.getIntroduce());
        shop.setCategory(category);
        shopRepository.save(shop);
        log.info("*** 쇼핑몰의 이름과 소개, 카테고리를 작성하였습니다. ***");

        // 쇼핑몰의 정보가 전부 작성된 상태라면 쇼핑몰을 개설 신청할 수 있다.
        String applyToAdmin = "개설 신청";
        // 쇼핑몰 정보를 저장.
        ShopApplication shopApplication = ShopApplication.builder()
                .application(shop.getUser().getUsername())
                .status(applyToAdmin)
                .shop(shop)
                .build();
        shopApplicationRepository.save(shopApplication);
        log.info("*** {}님의 쇼핑몰의 개설을 신청하였습니다. ***"
                , shop.getUser().getUsername());

        return ApplyShopDto.builder()
                .shopId(shop.getId())
                .shopName(shop.getName())
                .shopIntroduce(shop.getIntroduce() + "\n")
                .shopApplicationShopId(shopApplication.getShop().getId())
                .shopApplicationId(shopApplication.getId())
                .shopApplicationStatus(shopApplication.getStatus() + "\n")
                .categoryId(category.getId())
                .categoryName(category.getName()+ "\n")
                .build();
    }


    // 관리자는 개설 신청된 쇼핑몰의 목록을 확인할 수 있으며,
    public List<ShopApplicationDto> adminShowShopApplicationsList(
            String tokenUsername
    ) {
        log.info("*** 관리자는 쇼핑몰 개설 신청 목록을 확인 합니다. ***");

        // 사용자가 존재하는지 확인한다.
        User user = tokenUsername(tokenUsername );
        // 사용자의 권한이 admin인지 확인한다.
        // 1. admin이 아니라면 권한이 관리자가 아니라고 설정한다.
        if (!user.getRole().equals("ROLE_ADMIN")) {
            log.info("*** 사용자의 권한이 ADMIN이 아니어서 쇼핑몰 신청 목록을 확인할 수 없습니다. ***");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        // 2. admin이 맞다면 쇼핑몰 개설 신청 목록을 볼 수 있다.
        List<ShopApplication> shopApplicationList = shopApplicationRepository.findAll();
        List<ShopApplicationDto> dtoList = new ArrayList<>();
        for (ShopApplication s : shopApplicationList) {
            dtoList.add(ShopApplicationDto.fromEntity(s));
        }
        return dtoList;
    }

    // 관리자는 정보를 확인 후 허가 또는 불허 할 수 있다.
    // 1. 관리자는 정보를 확인한다.(신청자의 쇼핑몰 정보 확인.파라미터로ㄱㄱ)
    // 2. 쇼핑몰을 허가한다. ShopApplication.status("허가")
    // 2-1. shop.status("준비중 -> 허가")
    // 3. 쇼핑몰을 불허한다. ShopApplication.status("불허"),
    // ShopApplication.rejectReason("사유")
    // 3-1. shop.status("준비중 -> 불허"), shop.rejectReason("사유")

    // 1. 관리자는 정보를 확인한다.
    public ShopDto showShop(
            String tokenUsername,
            Long shopId
    ) {
        // 사용자가 존재하는지 확인한다.
        User username = tokenUsername(tokenUsername);
        // 쇼핑몰이 존재하는지 확인하다.
        Optional<Shop> findShop = shopRepository.findById(shopId);
        Shop shop = findShop.orElseThrow( () -> {
            log.info("*** 존재하지 않는 쇼핑몰 입니다. ***");
            return new ResponseStatusException(HttpStatus.NOT_FOUND);
        });

        // 사용자의 권한이 admin이거나 사용자여야 한다.
        if (username.getRole().equals("ROEL_ADMIN") ||
                username.getUsername().equals(shop.getUser().getUsername())) {
            log.info("*** 관리나 혹은 쇼핑몰 주인이 쇼핑몰 정보를 확인 한다. ***");
            return ShopDto.builder()
                    .status(shop.getStatus())
                    .name(shop.getName())
                    .introduce(shop.getIntroduce())
                    .id(shop.getId())
                    .build();
        }else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // 2. 관리자는 쇼핑몰을 허가한다. ShopApplication.status("허가")
    // 2-1. shop.status("준비중 -> 허가")
    public ShopApplicationConsentDto consent(
            String tokenUsername,
            Long shopId,
            String consent
    ) {
        // 존재하는 사용자인지 확인한다.
        User user = tokenUsername(tokenUsername);
        Optional<Shop> findShop = shopRepository.findById(shopId);
        Shop shop = findShop.orElseThrow( () -> {
            log.info("*** 존재하지 않는 쇼핑몰 입니다. ***");
            return new ResponseStatusException(HttpStatus.NOT_FOUND);
        });

        // 관리자가 아니라면 오류발생.
        if (!user.getRole().equals("ROLE_ADMIN")) {
            log.info("*** 사용자의 권한이 관리자가 아니므로 신청을 결정할 수 없습니다. ***");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Optional<ShopApplication> findEqualsShopId = shopApplicationRepository.findEqualsShopId(shopId);
        ShopApplication shopApplication = findEqualsShopId.orElseThrow(() -> {
                    log.info("*** 쇼핑몰 신청을 한 아이디가 아닙니다. ***");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND);
                });

        // 허가를 하고 싶으면.
        if (consent.equals("허가")) {
            // 관리자가 쇼핑몰을 허가하면
            shopApplication.setStatus(consent);
            shopApplicationRepository.save(shopApplication);
            log.info("*** 관리자가 쇼핑몰 개설을 허가 하였습니다. ***");

            String status = "오픈";
            // 쇼핑몰의 상태는 허가가 된다.
            shop.setStatus(status);
            shopRepository.save(shop);
            log.info("*** 쇼핑몰의 상태가 오픈으로 바뀌었습니다. ***");

            return ShopApplicationConsentDto.fromEntity(shopApplication, shop);

        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    // 3. 쇼핑몰을 불허한다. ShopApplication.status("불허"), ShopApplication.rejectReason("사유")
    // 3-1. shop.status("준비중 -> 불허"), shop.rejectReason("사유")
    public ShopApplicationConsentDto reject(
            String tokenUsername,
            Long shopId,
            String reject,
            ShopApplication rejectReason
    ) {
        // 존재하는 사용자인지 확인한다.
        User user = tokenUsername(tokenUsername);
        Optional<Shop> findShop = shopRepository.findById(shopId);
        Shop shop = findShop.orElseThrow( () -> {
            log.info("*** 존재하지 않는 쇼핑몰 입니다. ***");
            return new ResponseStatusException(HttpStatus.NOT_FOUND);
        });

        // 관리자가 아니라면 오류발생.
        if (!user.getRole().equals("ROLE_ADMIN")) {
            log.info("*** 사용자의 권한이 관리자가 아니므로 신청을 결정할 수 없습니다. ***");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Optional<ShopApplication> findEqualsShopId = shopApplicationRepository.findEqualsShopId(shopId);
        ShopApplication shopApplication = findEqualsShopId.orElseThrow(() -> {
            log.info("*** 쇼핑몰 신청을 한 아이디가 아닙니다. ***");
            return new ResponseStatusException(HttpStatus.NOT_FOUND);
        });

        // 불허를 하고 싶으면.
        if (reject.equals("불허")) {
            // 관리자가 쇼핑몰을 허가하면
            shopApplication.setStatus(reject);
            shopApplicationRepository.save(shopApplication);
            log.info("*** 관리자가 쇼핑몰 개설을 불허 하였고, 불허 사유를 작성 하였습니다.***");

            shop.setStatus(reject);
            shop.setRejectReason(rejectReason.getRejectReason());
            shopRepository.save(shop);
            log.info("*** 쇼핑몰의 상태가 불허 되었습니다. 불허 사유를 확인하세요. ***");


            return ShopApplicationConsentDto.fromEntity(shopApplication, shop);

        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }


    // 쇼핑몰을 등록하는 사용자가 존재하는 사용자인지 확인한다.
    public User tokenUsername(String tokenUsername) {
        Optional<User> username = userRepository.findByUsername(tokenUsername);
        return username.orElseThrow(()-> new UsernameNotFoundException(tokenUsername));
    }
}
