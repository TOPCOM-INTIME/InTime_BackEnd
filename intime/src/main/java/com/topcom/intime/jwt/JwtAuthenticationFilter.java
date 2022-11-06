package com.topcom.intime.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topcom.intime.auth.PrincipalDetails;
import com.topcom.intime.model.User;

import lombok.RequiredArgsConstructor;

//Spring Security에 UsernamePasswordAuthenticationFilter 가 있음
//login 요청해서 username, password POST하면 이 필터 동작
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final String secretKey;
	private final long tokenExpireTime;
	
	// /login 요청 후 로그인 시도를 위해 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		// 1.username, password 받아서
		System.out.println("JwtAuthenticationFilter : 로그인 시도중");

//		try {
//			BufferedReader br = request.getReader();
//			
//			String input = null;
//			while((input=br.readLine()) != null) {
//				System.out.println(input);
//			}
//			
//			System.out.println(request.getInputStream()); //getInputStream 안에 username/password 담겨있음
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		ObjectMapper om = new ObjectMapper();
		User user = null;
		try {
			user = om.readValue(request.getInputStream(), User.class);
			System.out.println(user);
			
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
			
			// PrincipalDetailsService의 loadUserByUsername() 실행
			Authentication authentication = 
					authenticationManager.authenticate(authenticationToken);

			// authentication이 생성되었다는 것은 DB 데이터와 로그인 Request 데이터가 일치하여 인증 되었다는 뜻, 즉 로그인 되었다는 뜻
			PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();

			return authentication; // authentication 객체가 Session 영역에 저장
											//리턴의 이유는 권한 관리를 Security가 대신 해주기 때문에 편하려고 하는거임
											//굳이 JWT토큰을 사용하면서 세션을 만들 이유가 없음. 근데 단지 권한 처리 때문에 session에 넣어준다.
		} catch (StreamReadException e) {
			e.printStackTrace();
		} catch (DatabindException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	// attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수 실행
	// JWT 토큰을 만들어서 reqeust요청한 사용자에게 JWT토큰을 response해주면 됨
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("successfulAuthentication 실행 : 인증 완료");
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();

		// JWT Token 생성
		// HASH 암호 방식
		String jwtToken = JWT.create()
				.withSubject("IntimeLoginToken")
				.withExpiresAt(new Date(System.currentTimeMillis() + tokenExpireTime))//10min
				.withClaim("id", principalDetails.getUser().getId())
				.withClaim("email", principalDetails.getUser().getEmail())
				.sign(Algorithm.HMAC512(secretKey));
		
		response.addHeader("Authorization", "Bearer " + jwtToken);
	}
}
