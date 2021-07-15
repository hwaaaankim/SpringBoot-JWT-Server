package com.dev.SecTwoJWT.config.jwt;

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
import com.dev.SecTwoJWT.config.auth.PrincipalDetails;
import com.dev.SecTwoJWT.dto.LoginRequestDto;
import com.dev.SecTwoJWT.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;


// UsernamePasswordAuthenticationFilter 필터는 원래 Security 가 /login 요청시 작동하는 filter이나
// config에서 formLogin() 을 disalbed를 시켰기 때문에 작동하지 않는다 
// 그래서 SecurityConfig에 새로 등록을 해 주어야 한다



// 초기화 되지 않는 final 필드나 @NonNull 이 붙은 필드에 대해 생성자를 생성 해 준다
// AuthenticationManager 생성자를 생성
@RequiredArgsConstructor 
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private final AuthenticationManager authenticationManager;
	
	// Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
	// 인증 요청시에 실행되는 함수 => /login요청을 하면 Login 시도를 위해서 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		System.out.println("JwtAuthenticationFilter : 진입");
		// 1. 입력된 username, password를 받는다
//		try {
//			BufferedReader br = request.getReader();
//			String input = null;
//			while((input=br.readLine())!=null) {
//				System.out.println(input);
//			}
//		username 과 password 가 x-www-form 형식으로 오는 경우
		
//			System.out.println(request.getInputStream());
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		// 2. 정상 Login 인지 확인  => authenticationManager를 통해서 Login 시도를 하면 PrincipalDetailsService 가 호출된다
		// loadUserByUsername() 메서드 실행
		
		// 3. 정상적으로 return 된 PrincipalDetails를 세션에 담는다
		// (세션을 만드는 이유는 권한을 관리하기 위해서)
		// 4. JWT토큰을 만들어서 응답
		
		// request에 있는 username과 password를 파싱해서 자바 Object로 받기
		ObjectMapper om = new ObjectMapper();
		try {
			User user = om.readValue(request.getInputStream(), User.class); 
			// 이렇게 하면 ObjectMapper 가 받은 Json 데이터를 user에 담아준다
			System.out.println(user);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					user.getUsername(), user.getPassword());
			// 받은 데이터를 가지고 Token 생성
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			// 받은 Token 을 authenticationManager를 사용해서 Login 인증 진행
			// 이게 실행 될 때 PrincipalDetailsService 의 loadUserByUsername() 메서드 실행 
			// 실행 된 메서드에 의해 얻어진 authentication 내에 있는 User정보 확인			
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			System.out.println("로그인 완료 : " + principalDetails.getUser().getUsername());
			// 출력이 된다는 것은 Session 내에 Authentication 객체 내에 PrincipalDetails 내에 정보가 저장 된 것
			// 즉 정상적 로그인을 의미
			// return 해주는 authentication 이 Session에 저장 된다
			// JWT를 사용하면서 세션을 만들 이유가 없으나 그 이유는 권한 처리를 Security가 대신 해줘서 편하기 때문
			return authentication;
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
//		LoginRequestDto loginRequestDto = null;
//		try {
//			loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println("JwtAuthenticationFilter : "+loginRequestDto);
//		
//		// 유저네임패스워드 토큰 생성
//		UsernamePasswordAuthenticationToken authenticationToken = 
//				new UsernamePasswordAuthenticationToken(
//						loginRequestDto.getUsername(), 
//						loginRequestDto.getPassword());
//		
//		System.out.println("JwtAuthenticationFilter : 토큰생성완료");
		
		// authenticate() 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의
		// loadUserByUsername(토큰의 첫번째 파라메터) 를 호출하고
		// UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과
		// UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
		// Authentication 객체를 만들어서 필터체인으로 리턴해준다.
		
		// Tip: 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입
		// Tip: 인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
		// 결론은 인증 프로바이더에게 알려줄 필요가 없음.
//		Authentication authentication = 
//				authenticationManager.authenticate(authenticationToken);
//		
//		PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
//		System.out.println("Authentication : "+principalDetailis.getUser().getUsername());
//		return authentication;
		return null;
	}


	// attemptAuthentication메서드가 실행 되서 정상적으로 인증이 완료 되면 successfulAuthentication메서드가 실행
	// JWT Token 생성해서 response에 담아서 사용자에게 보내준다
	// Hash 암호화 방식
	// HMAC512 방식의 특징은 Secret키를 가지고있어야 한다
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("로그인 인증 완료 => JWT 생성 시작");
		PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();
		
		String jwtToken = JWT.create()
				.withSubject(principalDetailis.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
				.withClaim("id", principalDetailis.getUser().getId())
				.withClaim("username", principalDetailis.getUser().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
		
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
	}
	
}