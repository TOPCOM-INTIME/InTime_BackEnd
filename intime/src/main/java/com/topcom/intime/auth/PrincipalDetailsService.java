package com.topcom.intime.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.topcom.intime.model.User;
import com.topcom.intime.repository.UserRepository;

import lombok.RequiredArgsConstructor;

//http://localhost:8000/login 요청 시 동작.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		System.out.println("loadUserByUsername 실행 : " +  email);
		User userEntity = userRepository.findByEmail(email)
				.orElseThrow(()->new UsernameNotFoundException("UserEmail not found with email : " + email));
		System.out.println("userEntity : " + userEntity);
		PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
		System.out.println("principalDetails : "+principalDetails);
		return principalDetails;//Return후 jwtAuthenticationFilter에서 사용예정
	}
	


}
