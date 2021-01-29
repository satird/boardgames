package ru.satird.boardgame.security.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.satird.boardgame.domain.AuthenticationProvider;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private final OAuth2User oAuth2User;
    private final AuthenticationProvider provider;

    public CustomOAuth2User(OAuth2User oAuth2User, AuthenticationProvider provider) {
        this.oAuth2User = oAuth2User;
        this.provider = provider;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
//        return (String) oAuth2User.getAttributes().get("name");
    }

    public String getEmail() {
        return oAuth2User.getAttribute("email");
//        return (String) oAuth2User.getAttributes().get("email");
    }

    public AuthenticationProvider getProvider() {
        return provider;
    }

    public String getGoogleId() {
        return oAuth2User.getAttribute("sub");
    }

    public String getUserpic() {
        return oAuth2User.getAttribute("picture");
    }

    public String getLocale() {
        return oAuth2User.getAttribute("locale");
    }
}
