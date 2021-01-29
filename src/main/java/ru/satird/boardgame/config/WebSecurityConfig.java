package ru.satird.boardgame.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.satird.boardgame.security.AuthProvider;
import ru.satird.boardgame.security.oauth.CustomOAuth2UserService;
import ru.satird.boardgame.security.oauth.OAuth2LoginSuccessHandler;
import ru.satird.boardgame.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthProvider authProvider;

    @Autowired
    private CustomOAuth2UserService oAuth2UserService;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/oauth2/**").permitAll()
                    .antMatchers("/", "/registration", "/login**", "/boardgames", "/static/**", "/resources/**", "/activate/*").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .oauth2Login()
                    .loginPage("/login").permitAll()
                    .userInfoEndpoint().userService(oAuth2UserService)
                .and()
                    .successHandler(oAuth2LoginSuccessHandler)
                .and()
                    .rememberMe()
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                    .permitAll();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

}
