package com.reza.events.config;

import com.reza.events.domain.entity.AuditUser;
import com.reza.events.spring.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditConfig {

    @Bean
    public AuditorAware<AuditUser> auditorProvider() {
        return new AuditorAwareImpl();
    }
}


