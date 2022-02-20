package de.thbingen.epro.controller.assembler;

import de.thbingen.epro.controller.businessunitobjective.BusinessUnitObjectiveController;
import de.thbingen.epro.controller.businessunit.BusinessUnitController;
import de.thbingen.epro.model.business.BusinessUnitObjective;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.mapper.BusinessUnitObjectiveMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BusinessUnitObjectiveAssembler implements RepresentationModelAssembler<BusinessUnitObjective, BusinessUnitObjectiveDto> {
    private final BusinessUnitObjectiveMapper mapper;

    public BusinessUnitObjectiveAssembler(BusinessUnitObjectiveMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public BusinessUnitObjectiveDto toModel(BusinessUnitObjective entity) {
        BusinessUnitObjectiveDto businessUnitObjectiveDto = mapper.businessUnitObjectiveToDto(entity)
                .add(linkTo(methodOn(BusinessUnitObjectiveController.class).findById(entity.getId())).withSelfRel())
                .add(linkTo(methodOn(BusinessUnitController.class).findById(entity.getBusinessUnit().getId())).withRel("businessUnit"));
        if(!entity.getBusinessUnitKeyResults().isEmpty()) {
            //businessUnitObjectiveDto.add(linkTo(methodOn(BusinessUnitOb)))
        }
        return businessUnitObjectiveDto;
    }
}
