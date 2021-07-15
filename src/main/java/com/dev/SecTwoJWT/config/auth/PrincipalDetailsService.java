package com.dev.SecTwoJWT.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dev.SecTwoJWT.model.User;
import com.dev.SecTwoJWT.repository.UserRepository;

import lombok.RequiredArgsConstructor;



// http://localhost:8080/login 요청시 동작하지 않는다 => formLogin() 을 disabled 했기 때문에
// 원래대로 라면 UsernamePasswordAuthenticationFilter 필터가 formLogin() 에서 진행이 되어야 하나 disalbed 했기 때문에 그 filter는
// 동작을 하지않기 때문에 다시 security에 등록을 해 주어야 한다
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService : 진입");
		User user = userRepository.findByUsername(username);

		// session.setAttribute("loginUser", user);
		return new PrincipalDetails(user);
	}
}

