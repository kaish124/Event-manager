package com.reza.events.modelmapper;

import com.reza.events.domain.entity.User;
import com.reza.events.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDto implements MappingConfigurer{
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.emptyTypeMap(User.class, UserDto.class)
                .addMappings(mapper -> {
                    mapper.map(User::getUserRoles, UserDto::setRoles);
                    mapper.skip(UserDto::setPassword);
                }).implicitMappings();

        modelMapper.emptyTypeMap(UserDto.class, User.class)
                .addMappings(mapper -> {
                    mapper.skip(User::setId); // Skip ID during mapping from DTO to entity
                    mapper.using(ctx -> {
                        String rawPassword = (String) ctx.getSource();
                        return passwordEncoder.encode(rawPassword);
                    }).map(UserDto::getPassword, User::setPassword);
                    mapper.map(UserDto::getRoles, User::setUserRoles);
                }).implicitMappings();
    }
}
