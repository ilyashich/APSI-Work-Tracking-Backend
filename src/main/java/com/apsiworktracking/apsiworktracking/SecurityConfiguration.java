package com.apsiworktracking.apsiworktracking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors().disable()
//                .and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/job/create").hasAuthority("USER")
                .antMatchers(HttpMethod.PUT, "/api/user/**/job_to_accept/**").hasAuthority("USER")
                .antMatchers(HttpMethod.PUT, "/api/user/**/job_to_accept_by_client/**").hasAuthority("USER")
                .antMatchers(HttpMethod.PUT, "/api/user/**/job_to_reject/**").hasAuthority("USER")
                .antMatchers(HttpMethod.PUT, "/api/user/**/job_to_reject_by_client/**").hasAuthority("USER")
                .antMatchers(HttpMethod.PUT, "/api/job/update/**").hasAuthority("USER")
                .antMatchers(HttpMethod.DELETE, "/api/job/delete/**").hasAuthority("USER")
                .antMatchers(HttpMethod.POST, "/api/project/create/**").hasAuthority("USER")
                .antMatchers(HttpMethod.PUT, "/api/project/update/**").hasAuthority("USER")
                .antMatchers(HttpMethod.POST, "/api/task/create/project/**").hasAuthority("USER")
                .antMatchers(HttpMethod.PUT, "/api/task/update/**").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "/api/invoice/get/**").hasAuthority("USER")
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers( "/index.html", "/", "/home", "/login", "/api/register")
                .permitAll()
                .anyRequest().authenticated()
                .and().logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .and().csrf().disable();
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
//                .and()
//                .sessionManagement();

    }


    @Bean
    public AuthenticationProvider authProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder());
        return provider;

    }



}
