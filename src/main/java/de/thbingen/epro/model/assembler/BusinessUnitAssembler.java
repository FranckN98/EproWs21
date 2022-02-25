package de.thbingen.epro.model.assembler;

import de.thbingen.epro.controller.BusinessUnitController;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.entity.BusinessUnit;
import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.model.mapper.BusinessUnitMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BusinessUnitAssembler implements RepresentationModelAssembler<BusinessUnit, BusinessUnitDto> {

    private final BusinessUnitMapper businessUnitMapper;

    public BusinessUnitAssembler(BusinessUnitMapper businessUnitMapper) {
        this.businessUnitMapper = businessUnitMapper;
    }

    @Override
    public BusinessUnitDto toModel(BusinessUnit entity) {
        OkrUser okrUser = (OkrUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        BusinessUnitDto businessUnitDto = businessUnitMapper.businessUnitToDto(entity)
                .add(linkTo(methodOn(BusinessUnitController.class).findById(entity.getId())).withSelfRel());
        if (entity.getBusinessUnitObjectives() != null && !entity.getBusinessUnitObjectives().isEmpty()) {
            businessUnitDto.add(linkTo(methodOn(BusinessUnitController.class)
                    .getAllBusinessUnitObjectives(null, entity.getId(), Optional.empty(), Optional.empty())).withRel("businessUnitObjectives"));
        }
        if (okrUser.hasPrivilege("view_users") && entity.getOkrUsers() != null && !entity.getOkrUsers().isEmpty()) {
            businessUnitDto.add(linkTo(methodOn(BusinessUnitController.class)
                    .getAllOkrUsers(null, entity.getId())).withRel("users"));
        }

        return businessUnitDto;
    }
}
