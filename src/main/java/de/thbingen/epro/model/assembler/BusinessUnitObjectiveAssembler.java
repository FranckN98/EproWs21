package de.thbingen.epro.model.assembler;

import de.thbingen.epro.controller.CompanyKeyResultController;
import de.thbingen.epro.controller.businessunit.BusinessUnitController;
import de.thbingen.epro.controller.businessunitkeyresult.BusinessUnitKeyResultController;
import de.thbingen.epro.controller.businessunitobjective.BusinessUnitObjectiveController;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.entity.BusinessUnitObjective;
import de.thbingen.epro.model.mapper.BusinessUnitObjectiveMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
        if (entity.getBusinessUnitKeyResults() != null && !entity.getBusinessUnitKeyResults().isEmpty()) {
            businessUnitObjectiveDto.add(entity.getBusinessUnitKeyResults().stream().map(businessUnitKeyResult -> linkTo(methodOn(BusinessUnitKeyResultController.class).findById(businessUnitKeyResult.getId())).withRel("businessUnitKeyResults")).collect(Collectors.toList()));
        }
        if (entity.getCompanyKeyResult() != null) {
            businessUnitObjectiveDto.add(linkTo(methodOn(CompanyKeyResultController.class).findById(entity.getCompanyKeyResult().getId())).withRel("companyKeyResult"));
        }
        return businessUnitObjectiveDto;
    }
}
