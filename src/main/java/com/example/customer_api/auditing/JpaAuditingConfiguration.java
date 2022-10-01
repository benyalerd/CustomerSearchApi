package com.example.customer_api.auditing;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
@Configuration
@EnableJpaAuditing(auditorAwareRef = "audiorProvider")
public class JpaAuditingConfiguration {
    @Bean
    public AuditorAware<String> audiorProvider(){
     return () -> {
var memberId = (String) RequestContextHolder.currentRequestAttributes()
.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
return Optional.ofNullable(memberId);
     };
    }
}