package de.thbingen.epro.model.assembler;

import de.thbingen.epro.controller.OkrUserController;
import de.thbingen.epro.controller.PrivilegeController;
import de.thbingen.epro.controller.RoleController;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.model.entity.Role;
import de.thbingen.epro.model.mapper.RoleMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RoleAssembler implements RepresentationModelAssembler<Role, RoleDto> {

    private final RoleMapper roleMapper;

    public RoleAssembler(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleDto toModel(Role entity) {
        RoleDto roleDto = roleMapper.roleToDto(entity)
                .add(linkTo(methodOn(RoleController.class).findById(entity.getId())).withSelfRel());
        if (entity.getOkrUsers() != null && !entity.getOkrUsers().isEmpty()) {
            roleDto.add(
                    entity.getOkrUsers().stream().map(okrUser -> linkTo(methodOn(OkrUserController.class).findById(okrUser.getId())).withRel("users")).collect(Collectors.toList())
            );
        }
        if (entity.getPrivileges() != null && !entity.getPrivileges().isEmpty()) {
            roleDto.add(
                    entity.getPrivileges().stream().map(privilege -> linkTo(methodOn(PrivilegeController.class).findById(privilege.getId())).withRel("privileges")).collect(Collectors.toList())
            );
        }
        return roleDto;
    }
}
