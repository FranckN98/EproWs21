package de.thbingen.epro.controller.assembler;

import de.thbingen.epro.controller.RoleController;
import de.thbingen.epro.model.business.Role;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.model.mapper.RoleMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

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
        return roleMapper.roleToDto(entity)
                .add(linkTo(methodOn(RoleController.class).findById(entity.getId())).withSelfRel());
    }
}
