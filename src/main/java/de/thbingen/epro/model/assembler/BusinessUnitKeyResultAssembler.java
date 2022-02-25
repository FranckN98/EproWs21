package de.thbingen.epro.model.assembler;

import de.thbingen.epro.controller.BusinessUnitKeyResultController;
import de.thbingen.epro.controller.BusinessUnitKeyResultHistoryController;
import de.thbingen.epro.controller.BusinessUnitObjectiveController;
import de.thbingen.epro.controller.CompanyKeyResultController;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.entity.BusinessUnitKeyResult;
import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.context.SecurityContextHolder;
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
        if (entity.getCompanyKeyResult() != null) {
            businessUnitKeyResultDto.add(linkTo(methodOn(CompanyKeyResultController.class).findById(entity.getCompanyKeyResult().getId())).withRel("companyKeyResult"));
        }
        if (entity.getBusinessUnitKeyResultHistories() != null && !entity.getBusinessUnitKeyResultHistories().isEmpty()) {
            businessUnitKeyResultDto.add(linkTo(methodOn(BusinessUnitKeyResultHistoryController.class).getAll(null)).withRel("history"));
        }
        return businessUnitKeyResultDto;
    }
}
