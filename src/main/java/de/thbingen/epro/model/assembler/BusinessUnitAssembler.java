package de.thbingen.epro.model.assembler;

import de.thbingen.epro.controller.BusinessUnitController;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.entity.BusinessUnit;
import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.model.mapper.BusinessUnitMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BusinessUnitAssembler implements RepresentationModelAssembler<BusinessUnit, BusinessUnitDto> {

    private final BusinessUnitMapper businessUnitMapper;
    private final AnnotationLinkRelationProvider annotationLinkRelationProvider;

    public BusinessUnitAssembler(BusinessUnitMapper businessUnitMapper, AnnotationLinkRelationProvider annotationLinkRelationProvider) {
        this.businessUnitMapper = businessUnitMapper;
        this.annotationLinkRelationProvider = annotationLinkRelationProvider;
    }

    @Override
    public BusinessUnitDto toModel(BusinessUnit entity) {
        OkrUser okrUser = (OkrUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        BusinessUnitDto businessUnitDto = businessUnitMapper.businessUnitToDto(entity)
                .add(linkTo(methodOn(BusinessUnitController.class).findById(entity.getId())).withSelfRel());
        if (entity.getBusinessUnitObjectives() != null && !entity.getBusinessUnitObjectives().isEmpty()) {
            businessUnitDto.add(linkTo(methodOn(BusinessUnitController.class)
                    .getAllBusinessUnitObjectives(null, entity.getId(), Optional.empty(), Optional.empty()))
                    .withRel(annotationLinkRelationProvider.getCollectionResourceRelFor(BusinessUnitObjectiveDto.class))
            );
        }
        if (okrUser.hasPrivilege("view_users") && entity.getOkrUsers() != null && !entity.getOkrUsers().isEmpty()) {
            businessUnitDto.add(linkTo(methodOn(BusinessUnitController.class)
                    .getAllOkrUsers(null, entity.getId()))
                    .withRel(annotationLinkRelationProvider.getCollectionResourceRelFor(OkrUserDto.class))
            );
        }

        return businessUnitDto;
    }
}
