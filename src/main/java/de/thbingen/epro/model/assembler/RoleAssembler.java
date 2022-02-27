package de.thbingen.epro.model.assembler;

import de.thbingen.epro.controller.RoleController;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.model.entity.Role;
import de.thbingen.epro.model.mapper.RoleMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RoleAssembler implements RepresentationModelAssembler<Role, RoleDto> {

    private final RoleMapper roleMapper;
    private final AnnotationLinkRelationProvider annotationLinkRelationProvider;

    public RoleAssembler(RoleMapper roleMapper, AnnotationLinkRelationProvider annotationLinkRelationProvider) {
        this.roleMapper = roleMapper;
        this.annotationLinkRelationProvider = annotationLinkRelationProvider;
    }

    @Override
    public RoleDto toModel(Role entity) {
        OkrUser okrUser = (OkrUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        RoleDto roleDto = roleMapper.roleToDto(entity)
                .add(linkTo(methodOn(RoleController.class).findById(entity.getId())).withSelfRel());

        if (okrUser.hasPrivilege("view_users") && entity.getOkrUsers() != null && !entity.getOkrUsers().isEmpty()) {
            roleDto.add(
                    linkTo(methodOn(RoleController.class).findAllUsersWithRole(null, entity.getId()))
                            .withRel(annotationLinkRelationProvider.getCollectionResourceRelFor(OkrUserDto.class))
            );
        }
        if (okrUser.hasPrivilege("access_privileges") && entity.getPrivileges() != null && !entity.getPrivileges().isEmpty()) {
            roleDto.add(
                    linkTo(methodOn(RoleController.class).findAllPrivilegesInRole(null, entity.getId()))
                            .withRel(annotationLinkRelationProvider.getCollectionResourceRelFor(PrivilegeDto.class))
            );
        }
        return roleDto;
    }
}
