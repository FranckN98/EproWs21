package de.thbingen.epro.model.assembler;

import de.thbingen.epro.controller.PrivilegeController;
import de.thbingen.epro.controller.RoleController;
import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.model.entity.Privilege;
import de.thbingen.epro.model.mapper.PrivilegeMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PrivilegeAssembler implements RepresentationModelAssembler<Privilege, PrivilegeDto> {

    private final PrivilegeMapper privilegeMapper;
    private final AnnotationLinkRelationProvider annotationLinkRelationProvider;

    public PrivilegeAssembler(PrivilegeMapper privilegeMapper, AnnotationLinkRelationProvider annotationLinkRelationProvider) {
        this.privilegeMapper = privilegeMapper;
        this.annotationLinkRelationProvider = annotationLinkRelationProvider;
    }

    @Override
    public PrivilegeDto toModel(Privilege entity) {
        OkrUser okrUser = (OkrUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PrivilegeDto privilegeDto = privilegeMapper.privilegeToDto(entity)
                .add(linkTo(methodOn(PrivilegeController.class).findById(entity.getId())).withSelfRel());

        if (okrUser.hasPrivilege("access_roles") && entity.getRoles() != null && !entity.getRoles().isEmpty()) {
            privilegeDto.add(
                    entity.getRoles().stream().map(role -> linkTo(methodOn(RoleController.class).findById(role.getId()))
                            .withRel(annotationLinkRelationProvider.getCollectionResourceRelFor(RoleDto.class)))
                            .collect(Collectors.toList())
            );
        }
        return privilegeDto;
    }
}
