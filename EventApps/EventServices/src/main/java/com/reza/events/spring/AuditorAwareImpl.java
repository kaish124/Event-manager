package com.reza.events.spring;

import com.reza.events.auth.security.SecurityUtil;
import com.reza.events.domain.entity.AuditUser;
import com.reza.events.security.AuthenticatedUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<AuditUser> {

    @Autowired
    private ModelMapper mapper;

    @Override
    public Optional<AuditUser> getCurrentAuditor() {
        AuditUser auditUser = null;
        AuthenticatedUser currentUser = SecurityUtil.getCurrentUser();
        if (currentUser != null && currentUser.getId() != null) {
            auditUser = mapper.map(currentUser, AuditUser.class);
        }
        return Optional.ofNullable(auditUser);
    }
}


