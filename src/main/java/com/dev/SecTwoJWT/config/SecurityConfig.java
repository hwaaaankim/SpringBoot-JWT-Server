package com.dev.SecTwoJWT.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.dev.SecTwoJWT.config.jwt.JwtAuthenticationFilter;
import com.dev.SecTwoJWT.config.jwt.JwtAuthorizationFilter;
import com.dev.SecTwoJWT.repository.UserRepository;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter{	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CorsConfig corsConfig;
	
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
    // 여기서 Filter를 등록하지 않고 컨트롤러에 @CrossOrigin 어노테이션을 사용 할 수도 있으나, 이 경우에는
    // 인증이 필요하지 않은 요청만 허용되고 인증이 필요한 요청은 거부된다.
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.addFilter(new FilterOne());
//		그냥 addFilter를 하는 경우 securityFilter가 아니기 때문에 SecurityConfig에 Filter로 등록이 안되고 addFilterBefore() 
//		혹은 addFilterAfter()를 통해 어떤 필터의 앞 혹은 뒤에 실행되도록 하라는 에러 메시지 출력
//		http.addFilterBefore(new FilterThree(), BasicAuthenticationFilter.class);
//		 이런 형태로 Filter 적용
//		https://velog.io/@sa833591/Spring-Security-5-Spring-Security-Filter-%EC%A0%81%EC%9A%A9 참고하여
//		기준이 되는 Filter를 선택 가능하다
// 		또다른 방법은 FilterConfig를 생성하여 적용 가능
//		다만 직접 등록한 Filter보다 Before / After 두가지 경우 모두 Security Filter가 먼저 실행 된다
		
//		org.springframework.beans.factory.BeanCreationException: Error creating bean with name 
//		'springSecurityFilterChain' defined in class path resource 
//		[org/springframework/security/config/annotation/web/configuration/WebSecurityConfiguration.class]: 
//		Bean instantiation via factory method failed; nested exception is 
//		org.springframework.beans.BeanInstantiationException: Failed to instantiate 
//		[javax.servlet.Filter]: Factory method 'springSecurityFilterChain' 
//		threw exception; nested exception is java.lang.IllegalArgumentException: 
//		The Filter class com.dev.SecTwoJWT.filterTest.FilterOne does not have a 
//		registered order and cannot be added without a specified order. 
//		Consider using addFilterBefore or addFilterAfter instead.
		

		http
			.addFilter(corsConfig.corsFilter())
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			// Session을 사용하지 않겠다는 뜻
		.and()
			.formLogin().disable() // form방식의 로그인 사용하지 않겠다 선언
			.httpBasic().disable() 
			// 기존의 Http 방식을 사용하지 않겠다 선언
			 
			.addFilter(new JwtAuthenticationFilter(authenticationManager())) 
// 			AuthenticationManager를 Filter에 전달 해 주어야 한다(UsernamePasswordAuthenticationFilter 는
//			AuthenticationManager를 통해서 Login 인증을 진행
//			WebSecurityConfigurerAdapter 가 authenticationManager() 를 가지고 있기 때문에 바로 전달 가능			
			.addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
			.authorizeRequests()
			.antMatchers("/api/v1/user/**")
			.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/api/v1/manager/**")
				.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/api/v1/admin/**")
				.access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll();
	}
}



















