package com.sujeong.oauth20.config.auth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * [ 큰 흐름 ]
 * 1. 구글 oauth 로그인 2. 로그인 완료 3. code를 리턴 4. AccessToken 요청
 * 5. userRequest 정보 받음 6. loadUser 함수 호출 7. 회원 프로필를 받아온다.
 */
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 구글로부터 받은 userRequest에 대해 후처리를 하는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        System.out.println("getClientRegistration" + userRequest.getClientRegistration()); // 어떤 oauth로 로그인했는지 확인 가능
//        System.out.println("getAccessToken" + userRequest.getAccessToken().getTokenValue());
//        System.out.println("getAttributes" + super.loadUser(userRequest).getAttributes()); // 이 함수를 통해 회원 정보 확인 가능
        OAuth2User oAuth2User = super.loadUser(userRequest);

        return super.loadUser(userRequest);
    }
}
