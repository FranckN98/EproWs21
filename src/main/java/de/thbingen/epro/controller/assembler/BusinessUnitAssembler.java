package de.thbingen.epro.controller.assembler;

import de.thbingen.epro.controller.BusinessUnitController;
import de.thbingen.epro.model.business.BusinessUnit;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.mapper.BusinessUnitMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BusinessUnitAssembler implements RepresentationModelAssembler<BusinessUnit, BusinessUnitDto> {

    private final Class<BusinessUnitController> controllerClass = BusinessUnitController.class;

    private final BusinessUnitMapper businessUnitMapper;

    public BusinessUnitAssembler(BusinessUnitMapper businessUnitMapper) {
        this.businessUnitMapper = businessUnitMapper;
    }

    @Override
    public BusinessUnitDto toModel(BusinessUnit entity) {
        return businessUnitMapper.businessUnitToDto(entity)
                .add(linkTo(methodOn(controllerClass).findById(entity.getId())).withSelfRel());
    }
}
