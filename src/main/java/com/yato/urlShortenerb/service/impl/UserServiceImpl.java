package com.yato.urlShortenerb.service.impl;

import com.yato.urlShortenerb.dto.RegisterRequest;
import com.yato.urlShortenerb.entity.User;
import com.yato.urlShortenerb.repo.UserRepo;
import com.yato.urlShortenerb.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo repo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> register(RegisterRequest request) {
        log.debug("Register request for {}", request.email());

        if (repo.findByEmail(request.email()).isPresent()) {
            log.warn("Email already exists: {}", request.email());
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        repo.save(user);

        log.info("User registered {}", request.email());
        return ResponseEntity.ok("User registered");
    }
}
