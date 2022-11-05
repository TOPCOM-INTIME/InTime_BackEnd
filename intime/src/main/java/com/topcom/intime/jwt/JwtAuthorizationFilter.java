package com.topcom.intime.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.topcom.intime.auth.PrincipalDetails;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.UserRepository;

// 권한이 필요한 요청이 들어올 때 해당 필터 거침
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final UserRepository userRepository;
	private final String secretKey;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, String secretKey) {
		super(authenticationManager);
		this.userRepository = userRepository;
		this.secretKey = secretKey;
	}

	// 해당 필터 실행시 위 생성자 함수 대신 아래 함수 실행
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String jwtHeader = request.getHeader("Authorization");

		// Header가 있는지 확인
		if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
			chain.doFilter(request, response);
			return;
		}

		// JWT 토큰을 검증해서 정상적인 사용자인지 확인
		String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
		
		String email = 
				JWT.require(Algorithm.HMAC512(secretKey)).build().verify(jwtToken).getClaim("email").asString();
		
		if(email != null) {
			User userEntity = userRepository.findByEmail(email);

			PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
			
			// JWT 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
			Authentication authentication = 
					new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

			// 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);

			chain.doFilter(request, response);
			
		}
	}
	
}
