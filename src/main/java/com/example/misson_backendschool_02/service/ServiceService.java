package com.example.misson_backendschool_02.service;

import com.example.misson_backendschool_02.dto.UserDto;
import com.example.misson_backendschool_02.entity.User;
import com.example.misson_backendschool_02.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceService {
    private final UserRepository userRepository;

//    public Boolean createComments(UserDto userDto) {
//        // 권한이 INACTIVE라면 다른 튜플 정보 입력 후 MEMBER로 권한 변경.
//        return userDto.getRole().equals("ROLE_INACTIVE");
//    }

    public UserDto changeToRoleMember(
            String tokenUsername,
            String name,
            String nickname,
            Integer age,
            String email,
            Integer phone
    ) {
        // 권한을 member로 설정.
        String role = "ROLE_MEMBER";

        // 토큰을 파싱하여 사용자 정보 추출하기.
        Optional<User> username = userRepository.findByUsername(tokenUsername);

        User usernameInToken = username.orElseThrow(() -> new UsernameNotFoundException(tokenUsername));

        log.info("*** 토큰파싱: {} ***", usernameInToken.getUsername());

        // 토큰에서 가져온 user엔티티를 업데이트 한다.
        usernameInToken.setRole(role);
        usernameInToken.setName(name);
        usernameInToken.setNickname(nickname);
        usernameInToken.setAge(age);
        usernameInToken.setEmail(email);
        usernameInToken.setPhone(phone);

        // UserEntity log찍기 위함
        Map<String, ? extends Serializable> properties = Map.of(
                "Role", usernameInToken.getRole(),
                "Name", usernameInToken.getName(),
                "Nickname", usernameInToken.getNickname(),
                "Age", usernameInToken.getAge(),
                "Email", usernameInToken.getEmail(),
                "Phone", usernameInToken.getPhone(),
                "Username", usernameInToken.getUsername(),
                "Password", usernameInToken.getPassword(),
                "Id", usernameInToken.getId()
        );


        log.info("*** 서비스를 사용하기 위한 Member로 권한 변경 ***");
        log.info("*** user: {}, role: {} ***", usernameInToken.getUsername(), usernameInToken.getRole());
        // UserEntity 출력.
        properties.forEach((key, value) -> {
            log.info("{}, {}", key, value);
        });
        
        // 디비에 저장
        userRepository.save(usernameInToken);

        return UserDto.fromEntityMember(usernameInToken, role);
    }

}
