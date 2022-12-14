package com.sujeong.oauth20.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sujeong.oauth20.config.auth.PrincipalDetails;
import com.sujeong.oauth20.model.KakaoProfile;
import com.sujeong.oauth20.model.OauthToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("PrincipalDetails" + principalDetails.getUser());
        return "한 타입으로 세션정보 확인";
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(@RequestParam String code) {

        String reqUrl = "https://kauth.kakao.com/oauth/token";
        RestTemplate rt = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "94ff8c81132286d85f870b98a437f2b3");
        params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
        params.add("code", code);

        // HttpHeader와 HttpBody를 하나의 오브젝트로 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // Post로 Http 요청
        ResponseEntity response = rt.exchange(
                reqUrl,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue((String) response.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        /**
         * 사용자 정보 조회
         */
        RestTemplate rt2 = new RestTemplate();

        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

        ResponseEntity response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue((String)response2.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

//        System.out.println("카카오 아이디" + kakaoProfile.getId());
//        System.out.println("카카오 이메일" + kakaoProfile.getKakao_account().getEmail());

        return "카카오 인증 완료 후 응답 - " + response;
    }
}
