package com.reza.events.modelmapper;

import com.reza.events.domain.entity.UserRole;
import com.reza.events.dto.RoleBean;
import com.reza.events.enums.UserRoleType;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserRoleToRoleBean implements MappingConfigurer {
    @Override
    public void configure(ModelMapper modelMapper) {
        // UserRole to RoleBean mapping
        modelMapper.emptyTypeMap(UserRole.class, RoleBean.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getRole().getId(), RoleBean::setId);
                    mapper.map(src -> src.getRole().getName(), RoleBean::setName);
                    mapper.map(src -> src.getRole().getDescription(), RoleBean::setDescription);
                    mapper.map(src -> src.getRole().getType(), RoleBean::setType);
                    mapper.map(UserRole::getId, RoleBean::setUserRoleId);
                }).implicitMappings();

        // RoleBean to Role mapping (reverse mapping)
        modelMapper.emptyTypeMap(RoleBean.class, UserRole.class)
                .addMappings(mapper -> {
                    mapper.map(RoleBean::getId, (dest, val) -> dest.getRole().setId((Long) val));
                    mapper.map(RoleBean::getName, (dest, val) -> dest.getRole().setName((String) val));
                    mapper.map(RoleBean::getDescription, (dest, val) -> dest.getRole().setDescription((String) val));
                    mapper.map(RoleBean::getType, (dest, val) -> dest.getRole().setType((UserRoleType) val));
                    mapper.map(RoleBean::getUserRoleId, UserRole::setId);
                }).implicitMappings();
    }
}