package com.sujeong.oauth20.controller;

import com.sujeong.oauth20.model.User;
import com.sujeong.oauth20.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/join")
    public String join() {
        return "joinForm";
    }

    @PostMapping("/join")
    public @ResponseBody String join(User user) {
        user.setRole("USER");
        String rawPwd = user.getPassword();
        String encryptPwd = bCryptPasswordEncoder.encode(rawPwd);
        user.setPassword(encryptPwd);
        userRepository.save(user);
        return "join";
    }
}
