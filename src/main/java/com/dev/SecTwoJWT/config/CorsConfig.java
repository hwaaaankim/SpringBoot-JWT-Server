package com.dev.SecTwoJWT.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

   @Bean
   public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true); 
      // 내 서버가 응답을 할 때 json을 javascript에서 처리할 수 있게 할지를 설정 하는 것
      // false일 경우 javascript로 요청이 오면 응답을 하지 않는다
      config.addAllowedOrigin("*"); 
      // e.g. http://domain1.com
      // 모든 IP응답을 허용 하겠다는 것
      config.addAllowedHeader("*");
      // 모든 Header에 응답 하겠다 선언
      config.addAllowedMethod("*");
      // 모든 메서드 GET / POST / DELETE / PUT 요청을 허용

      source.registerCorsConfiguration("/api/**", config);
      return new CorsFilter(source);
   }

}
