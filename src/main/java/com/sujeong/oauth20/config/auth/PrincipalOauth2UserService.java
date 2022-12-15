package com.sujeong.oauth20.config.auth;

import com.sujeong.oauth20.model.User;
import com.sujeong.oauth20.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 구글로부터 받은 userRequest에 대해 후처리를 하는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        System.out.println("getClientRegistration" + userRequest.getClientRegistration()); // 어떤 oauth로 로그인했는지 확인 가능
//        System.out.println("getAccessToken" + userRequest.getAccessToken().getTokenValue());
//        System.out.println("getAttributes" + super.loadUser(userRequest).getAttributes()); // 이 함수를 통해 회원 정보 확인 가능
        OAuth2User oauth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getClientId(); // 구글
        String providerId = oauth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("비밀번호");
        String email = oauth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            System.out.println("최초 로그인입니다.");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        } else {
            System.out.println("이미 로그인 한 적이 있습니다.");
        }

        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
}
