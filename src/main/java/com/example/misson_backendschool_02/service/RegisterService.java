package com.example.misson_backendschool_02.service;

import com.example.misson_backendschool_02.dto.RegisterDto;
import com.example.misson_backendschool_02.entity.RegisterEntity;
import com.example.misson_backendschool_02.entity.User;
import com.example.misson_backendschool_02.repo.RegisterRepository;
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
@Slf4j
@RequiredArgsConstructor
public class RegisterService {
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;

    public RegisterDto addRegisterItem( // 물건 최초 등록. 상태-판매중
            String token,
            String title,
            String content,
            Integer minPrice
    ) {
        Optional<User> tokenUsername = userRepository.findByUsername(token);
        User username = tokenUsername.orElseThrow(() -> new UsernameNotFoundException(token));
        log.info("*** 작성자의 username: {} ***", username.getUsername());

        RegisterEntity registerEntity = RegisterEntity.builder()
                .title(title)
                .content(content)
                .minPrice(minPrice)
                .user(username) //등록된 물품 정보는 작성자가 수정, 삭제 가능하게 위함.
                .build();

        String statusItem = "판매중";
        registerEntity.setStatus(statusItem); // 상태 - 판매중

        // 저장하기.
        registerRepository.save(registerEntity);

        return RegisterDto.fromEntityRegister(registerEntity, statusItem);
    }

    // 등록한 물품 정보는 비활성 사용자를 제외 누구든지 열람할 수 있다.
    public RegisterDto registerBoards(Long registerId, String token ) {
        // 레지스터 id가 있다면 출력, 없다면 status.NOt_Found
        Optional<RegisterEntity> findRegisterEntity  = registerRepository.findById(registerId);

        RegisterEntity registerEntity =
                findRegisterEntity.orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("*** 존재하는 register id입니다. ***");

        // User 정보도 확인해야지 비활성 사용자인지 아닌지 확인할 수 있다.
        Optional<User> tokenUsername = userRepository.findByUsername(token);
        User username = tokenUsername.orElseThrow(() -> new UsernameNotFoundException(token));
        log.info("*** 존재하는 User입니다. ***");
        log.info("*** username: {}, role: {} ***", username.getUsername(), username.getRole());

        return RegisterDto.fromEntityRegister(registerEntity);

    }

    // 등록된 물품 정보는 작성자가 수정, 삭제 가능하다.

    // 수정
    public Boolean updateCheck(String token, Long id) {
        // User이 맞는지 확인한다.
        Optional<User> tokenUsername = userRepository.findByUsername(token);
        User username = tokenUsername.orElseThrow(()-> new UsernameNotFoundException(token));
        // 레지스터 id가 있나 없나 확인한다.
        Optional<RegisterEntity> findRegisterEntity = registerRepository.findById(id);
        RegisterEntity registerEntity =
                findRegisterEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 사용자와 게시글의 작성자가 같은지 확인.
        log.info("*** register.getUser().getUsername(): {} ***", registerEntity.getUser().getUsername());
        log.info("*** username.getUsername(): {}", username.getUsername());

        // 게시글의 작성자인지 아닌지 확인한다.
        if (registerEntity.getUser().getUsername()
                .equals(username.getUsername())){
            log.info("*** 게시글을 작성한 user입니다. ***");
            return true;
        }
        log.info("*** 게시글을 작성한 username이 아닙니다. ***");
        return false;
    }

    // 게시글의 작성자가 맞는지 확인 후 수정한다.
    public RegisterDto updateItem(
            String token,
            Long id,
            String title,
            String content,
            Integer minPrice
    ) {
        Boolean update = updateCheck(token, id);
        if (update) {
            Optional<RegisterEntity> findRegisterEntity = registerRepository.findById(id);
            RegisterEntity registerEntity = findRegisterEntity.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));

            registerEntity.setTitle(title);
            registerEntity.setContent(content);
            registerEntity.setMinPrice(minPrice);

            registerRepository.save(registerEntity);

            return RegisterDto.fromEntityRegister(registerEntity);
        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    // 게시글의 작성자가 맞다면 삭제가 가능하다.
    public void deleteItem(
            String token,
            Long id
    ) {
        Boolean update = updateCheck(token, id);
        if (update) {
            registerRepository.deleteById(id);
        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }


}
