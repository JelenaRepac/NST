package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.domain.Role;
import nst.springboot.nstapplication.dto.RoleDto;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter implements DtoEntityConverter<RoleDto, Role> {
    @Override
    public RoleDto toDto(Role role) {
       return RoleDto.builder().
               id(role.getId()).
               name(role.getName()).
               build();
    }

    @Override
    public Role toEntity(RoleDto roleDto) {
        return Role.builder().
                id(roleDto.getId()).
                name(roleDto.getName()).
                build();
    }
}
