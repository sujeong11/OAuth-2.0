package com.sujeong.oauth20.config.oauth;

import com.sujeong.oauth20.config.auth.PrincipalDetails;
import com.sujeong.oauth20.config.oauth.provider.FacebookUserInfo;
import com.sujeong.oauth20.config.oauth.provider.GoogleUserInfo;
import com.sujeong.oauth20.config.oauth.provider.NaverUserInfo;
import com.sujeong.oauth20.config.oauth.provider.Oauth2UserInfo;
import com.sujeong.oauth20.model.User;
import com.sujeong.oauth20.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

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
        System.out.println("getClientRegistration" + userRequest.getClientRegistration()); // 어떤 oauth로 로그인했는지 확인 가능
        System.out.println("getAccessToken" + userRequest.getAccessToken().getTokenValue());
        System.out.println("getAttributes" + super.loadUser(userRequest).getAttributes()); // 이 함수를 통해 회원 정보 확인 가능
        OAuth2User oauth2User = super.loadUser(userRequest);
        Oauth2UserInfo oauth2UserInfo = null;

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oauth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페이스북 로그인 요청");
            oauth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인 요청");
            // getAttributes() 안의 response() 안에 id, email, name 등이 있으므로
            oauth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
        } else {
            System.out.println("저희 서비스는 구글, 페이스북, 네이버 로그인만 지원합니다.");
        }

        String provider = oauth2UserInfo.getProvider();
        String providerId = oauth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("비밀번호");
        String email = oauth2UserInfo.getEmail();
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            System.out.println("Oauth 로그인이 최초입니다.");
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
            System.out.println("Oauth로 이미 로그인 한 적이 있습니다.(자동 회원가입)");
        }

        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
}
