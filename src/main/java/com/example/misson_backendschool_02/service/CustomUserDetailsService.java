package com.example.misson_backendschool_02.service;

import com.example.misson_backendschool_02.entity.CustomUserDetails;
import com.example.misson_backendschool_02.entity.User;
import com.example.misson_backendschool_02.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException(username);

        User user = optionalUser.get();
        log.info("*** 로그인한 사용자 정보: {}, {} ***", user.getUsername(), user.getRole());

        return new CustomUserDetails(user);
    }
}
