package de.thbingen.epro.controller.assembler;

import de.thbingen.epro.controller.PrivilegeController;
import de.thbingen.epro.model.business.Privilege;
import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.mapper.PrivilegeMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

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
        return privilegeMapper.privilegeToDto(entity)
                .add(linkTo(methodOn(PrivilegeController.class).findById(entity.getId())).withSelfRel());
    }
}
