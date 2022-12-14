package com.sujeong.oauth20.controller;

import com.sujeong.oauth20.config.auth.PrincipalDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthController {

    @GetMapping("/test/login")
    public String testLogin(Authentication authentication,
                            @AuthenticationPrincipal PrincipalDetails userDetails) { // PrincipalDetails가 implements UserDetails 해서 가능
        System.out.println("/test/login -----------------------");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication: " + principalDetails.getUser());

        System.out.println("UserDetails: " + userDetails.getUser());
        return "사용자 세션정보";
    }

    @GetMapping("/test/oauth/login")
    public String testOauthLogin(Authentication authentication,
                            @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("/test/oauth/login -----------------------");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication: " + oAuth2User.getAttributes());

        System.out.println("OAuth2User: " + oauth.getAttributes());
        return "Oauth 사용자 세션정보";
    }
}
