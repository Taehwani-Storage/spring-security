package com.bit.security.service;

import com.bit.security.model.AuthDTO;
import com.bit.security.model.KakaoMemberInfo;
import com.bit.security.model.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class OAuthService extends DefaultOAuth2UserService {
    @Autowired
    private final BCryptPasswordEncoder PASSWORD_ENCODER;
    @Autowired
    private final UserService USER_SERVICE;
    @Autowired
    private UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) {
        OAuth2User oAuth2User = super.loadUser(req);
        KakaoMemberInfo memberInfo = new KakaoMemberInfo(oAuth2User.getAttributes());

        String email = memberInfo.getEmail();

        UserDTO userDTO = userService.selectByEmail(email);
        System.out.println(userDTO);
        if (userDTO != null) {
            new AuthDTO(userDTO, memberInfo.getAttributes());
        }

        return oAuth2User;
    }
}
