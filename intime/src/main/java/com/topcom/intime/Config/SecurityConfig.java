package com.topcom.intime.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

import com.topcom.intime.jwt.JwtAuthenticationFilter;
import com.topcom.intime.jwt.JwtAuthorizationFilter;
import com.topcom.intime.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final CorsFilter corsFilter;
	private final UserRepository userRepository;
	
	@Value("${jwt.secretKey}")
	private String secretKey;
	@Value("${jwt.expiration-milliseconds}")
	private long tokenExpireTime;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용하지 않음
		.and()
//		.addFilter(corsFilter)//모든 요청 CorsConfig에서 만든 corsFilter 거침
		.formLogin().disable()
		.httpBasic().disable()
		.addFilter(new JwtAuthenticationFilter(authenticationManager(), secretKey, tokenExpireTime))
		.addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository, secretKey))
		.authorizeRequests()
		.antMatchers("/api/**")		
		.access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
		.antMatchers("/admin/**")
//		.access("hasRole('ROLE_ADMIN')")
//		.antMatchers("/advertiser/**")
		.access("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADVERTISER')")
        .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
        .permitAll()
		.anyRequest().permitAll();
	}
	
	
}
