package edu.tomerbu.lec4tdd.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//Spring detects this class during component scan:
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthUserService authUserService;

    public SecurityConfiguration(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authUserService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.cors();
        http.csrf().disable();

        http.httpBasic().authenticationEntryPoint(new AuthEntryPoint());
        //h2 console is blocked:
        //h2 console uses iframes: we need to enable iFrames Support. blocked by default.
        http.headers().frameOptions().disable();
        //http.headers().frameOptions().sameOrigin();

        http.httpBasic()
                .and()
                .authorizeHttpRequests().antMatchers(HttpMethod.POST, "/api/1/login").authenticated()
                .and()
                .authorizeHttpRequests().anyRequest().permitAll();

        //RESTFULL APIS should be stateless.
        //user data will be saved in other ways: LIKE Database
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    //now we can inject this passwordEncoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
