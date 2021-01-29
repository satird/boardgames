package ru.satird.boardgame.security.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import ru.satird.boardgame.domain.AuthenticationProvider;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        AuthenticationProvider provider = AuthenticationProvider.LOCAL;
        switch (userRequest.getClientRegistration().getRegistrationId()) {
            case ("facebook"):
                 provider = AuthenticationProvider.FACEBOOK;
                 break;
            case ("google"):
                provider = AuthenticationProvider.GOOGLE;
                break;
        }
//        System.out.println("USER : " + userRequest.getClientRegistration().getRegistrationId());
        return new CustomOAuth2User(user, provider);
    }
}
