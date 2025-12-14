package com.yato.urlShortenerb.controller;


import com.yato.urlShortenerb.config.JWTUtils;
import com.yato.urlShortenerb.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final JWTUtils jwtUtils;

    @GetMapping("/overview")
    public ResponseEntity<?> getAnalytics(HttpServletRequest request){
        String auth= request.getHeader("Authorization");
        if(auth==null || !auth.startsWith("Bearer "))
            return ResponseEntity.status(401).body("Missing Token");

        String token= auth.substring(7);
        String email= jwtUtils.getEmailFromToken(token);

        return analyticsService.getAnalytics(email);
    }


}
