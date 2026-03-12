package com.reza.events.modelmapper;

import com.reza.events.domain.entity.User;
import com.reza.events.domain.entity.UserRole;
import com.reza.events.security.AuthenticatedUser;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;

public class UserToAuthenticatedUser implements MappingConfigurer{
    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.emptyTypeMap(User.class, AuthenticatedUser.class)
                .addMappings(mapper -> {
                    mapper.using(ctx -> {
                        return ((Set<UserRole>) ctx.getSource()).stream()
                                .map(ur -> ur.getRole().getType().name())
                                .distinct()
                                .collect(Collectors.toList());
                    }).map(User::getUserRoles, AuthenticatedUser::setRoles);
                }).implicitMappings();
    }
}
