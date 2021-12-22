package ru.alex.mini_bank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.alex.mini_bank.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement().maximumSessions(1)
                .expiredUrl("/ex")
                .and().invalidSessionUrl("/inv")
                .sessionFixation().newSession();

        httpSecurity
                .authorizeRequests()
                //Доступ только для не зарегистрированных пользователей
                .antMatchers("/status", "/signIn", "/signUp").permitAll()
                //Все остальные страницы требуют аутентификации
                .anyRequest().authenticated();

        httpSecurity
                .formLogin()
                .usernameParameter("uniqueName")
                .passwordParameter("password")
                .loginProcessingUrl("/signIn")
                .successForwardUrl("/status")
                .defaultSuccessUrl("/status")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }
}
