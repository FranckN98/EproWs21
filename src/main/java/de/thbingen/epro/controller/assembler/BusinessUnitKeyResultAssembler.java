package de.thbingen.epro.controller.assembler;

import de.thbingen.epro.controller.businessunitkeyresult.BusinessUnitKeyResultController;
import de.thbingen.epro.controller.businessunitobjective.BusinessUnitObjectiveController;
import de.thbingen.epro.model.business.BusinessUnitKeyResult;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BusinessUnitKeyResultAssembler implements RepresentationModelAssembler<BusinessUnitKeyResult, BusinessUnitKeyResultDto> {

    private final BusinessUnitKeyResultMapper mapper;

    public BusinessUnitKeyResultAssembler(BusinessUnitKeyResultMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public BusinessUnitKeyResultDto toModel(BusinessUnitKeyResult entity) {
        BusinessUnitKeyResultDto businessUnitKeyResultDto = mapper.businessUnitKeyResultToDto(entity)
                .add(linkTo(methodOn(BusinessUnitKeyResultController.class).findById(entity.getId())).withSelfRel());
        if (entity.getBusinessUnitObjective() != null) {
            businessUnitKeyResultDto.add(
                    linkTo(
                            methodOn(BusinessUnitObjectiveController.class)
                                    .findById(entity.getBusinessUnitObjective()
                                            .getId()))
                            .withRel("businessUnitObjective")
            );
        }
        if (entity.getBusinessUnitKeyResultHistories() != null && !entity.getBusinessUnitKeyResultHistories().isEmpty()) {
            //TODO
        }
        return businessUnitKeyResultDto;
    }
}
