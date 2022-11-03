package com.topcom.intime.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.topcom.intime.filter.MyFilter1;
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

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용하지 않음
		.and()
		.addFilter(corsFilter)//모든 요청 CorsConfig에서 만든 corsFilter 거침
		.formLogin().disable()
		.httpBasic().disable()
		.addFilter(new JwtAuthenticationFilter(authenticationManager()))
		.addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
		.authorizeRequests()
		.antMatchers("/api/user/**")		
		.access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
		.antMatchers("/admin/**")
		.access("hasRole('ROLE_ADMIN')")
		.anyRequest().permitAll();
		
	}
	
}
