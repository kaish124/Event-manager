package com.reza.events.modelmapper;

import com.reza.events.domain.entity.User;
import com.reza.events.domain.entity.UserRole;
import com.reza.events.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserToUserBean implements MappingConfigurer{
    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.emptyTypeMap(User.class, UserDto.class)
                .addMappings(mapper -> {
                    mapper.using(ctx -> {
                        return ((Set<UserRole>) ctx.getSource()).stream()
                                .map(ur -> ur.getRole().getType().name())
                                .distinct()
                                .collect(Collectors.toList());
                    }).map(User::getUserRoles, UserDto::setRoles);
                }).implicitMappings();
    }
}
