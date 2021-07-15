package com.dev.SecTwoJWT.filterTest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FilterOne implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//		System.out.println(request);
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
//		req.setCharacterEncoding("UTF-8");
//		System.out.println(req.getMethod());
//		if(req.getMethod().equals("POST")) {
//			String headerAuth = req.getHeader("Authorization");
//			System.out.println(headerAuth);
//		}
//		토큰 : hwan 이라고 가정 하고 해당 값이 header에 Authorization에 정상적으로 들어오면 접근이 가능하고 
//		정상적이지 않은 경우 Controller에 접근할 수 없도록 테스트
		if(req.getHeader("Authorization").equals("hwan")){
			System.out.println(req.getHeader("Authorization"));
			chain.doFilter(req, res);
		}else {
			PrintWriter outPrintWriter = res.getWriter();
			outPrintWriter.println("인증 불가");
		}
		
//		ID/PW가 정상적으로 와서 Login인증이 완료 된 시점에 Token을 생성하고 응답 해 준다
//		요청시 마다 Header에 Authorizaion 에 value 에 Token을 가지고 데이터를 주고받고
//		받은 Token에 대해서 RSA, HS256 방식을 이용해서 검증만 하면 된다
//		System.out.println("Filter ONE");
		
	}
}




















