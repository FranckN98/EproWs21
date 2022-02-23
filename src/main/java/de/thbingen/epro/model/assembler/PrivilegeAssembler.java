package de.thbingen.epro.model.assembler;

import de.thbingen.epro.controller.PrivilegeController;
import de.thbingen.epro.controller.RoleController;
import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.entity.Privilege;
import de.thbingen.epro.model.mapper.PrivilegeMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PrivilegeAssembler implements RepresentationModelAssembler<Privilege, PrivilegeDto> {

    private final PrivilegeMapper privilegeMapper;

    public PrivilegeAssembler(PrivilegeMapper privilegeMapper) {
        this.privilegeMapper = privilegeMapper;
    }

    @Override
    public PrivilegeDto toModel(Privilege entity) {
        PrivilegeDto privilegeDto = privilegeMapper.privilegeToDto(entity)
                .add(linkTo(methodOn(PrivilegeController.class).findById(entity.getId())).withSelfRel());
        if (entity.getRoles() != null && !entity.getRoles().isEmpty()) {
            privilegeDto.add(
                    entity.getRoles().stream().map(role -> linkTo(methodOn(RoleController.class).findById(role.getId())).withRel("roles")).collect(Collectors.toList())
            );
        }
        return privilegeDto;
    }
}
