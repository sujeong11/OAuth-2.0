package com.sujeong.oauth20.config.auth;

import com.sujeong.oauth20.model.User;
import com.sujeong.oauth20.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * [ 규칙 ]
 * 시큐리티 설정 파일에서 loginProcessingUrl("")를 사용하면
 * 로그인 요청이 올 때 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // username이 아닌 다른 이름으로 사용하고 싶다면 시큐리티 설정 파일에서 usernameParameter()로 원하는 값을 지정해줘야 한다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userRepository.findByUsername(username);
        if (findUser != null) {
            return new PrincipalDetails(findUser);
        }
        return null;
    }
}
