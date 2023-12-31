package com.springboot.ecommerce.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception{
        // protect endpoints /api/orders
        http.authorizeHttpRequests(configurer->{
            configurer.requestMatchers("/api/orders/**").authenticated()
                    .requestMatchers("/**").permitAll();;

        }).oauth2ResourceServer().jwt();
        // add CORS filters
        http.cors();
        // add content negotiation strategy
        http.setSharedObject(ContentNegotiationStrategy.class,
                new HeaderContentNegotiationStrategy());
        // force a non-empty response body for 401's to user friendly response
        Okta.configureResourceServer401ResponseBody(http);
        return http.build();
    }
}
