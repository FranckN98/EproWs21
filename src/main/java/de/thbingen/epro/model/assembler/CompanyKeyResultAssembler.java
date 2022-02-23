package de.thbingen.epro.model.assembler;

import de.thbingen.epro.controller.CompanyKeyResultController;
import de.thbingen.epro.controller.CompanyKeyResultHistoryController;
import de.thbingen.epro.controller.CompanyObjectiveController;
import de.thbingen.epro.controller.businessunitkeyresult.BusinessUnitKeyResultController;
import de.thbingen.epro.controller.businessunitobjective.BusinessUnitObjectiveController;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.entity.CompanyKeyResult;
import de.thbingen.epro.model.mapper.CompanyKeyResultMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CompanyKeyResultAssembler implements RepresentationModelAssembler<CompanyKeyResult, CompanyKeyResultDto> {

    private final CompanyKeyResultMapper companyKeyResultMapper;

    public CompanyKeyResultAssembler(CompanyKeyResultMapper companyKeyResultMapper) {
        this.companyKeyResultMapper = companyKeyResultMapper;
    }

    @Override
    public CompanyKeyResultDto toModel(CompanyKeyResult entity) {
        CompanyKeyResultDto companyKeyResultDto = companyKeyResultMapper.companyKeyResultToDto(entity)
                .add(linkTo(methodOn(CompanyKeyResultController.class).findById(entity.getId())).withSelfRel());
        if (entity.getCompanyObjective() != null) {
            companyKeyResultDto.add(linkTo(methodOn(CompanyObjectiveController.class).findById(entity.getCompanyObjective().getId())).withRel("companyObjective"));
        }
        if (entity.getCompanyKeyResultHistories() != null && !entity.getCompanyKeyResultHistories().isEmpty()) {
            companyKeyResultDto.add(linkTo(methodOn(CompanyKeyResultHistoryController.class).getAll(null)).withRel("history"));
        }
        if (entity.getBusinessUnitObjectives() != null && !entity.getBusinessUnitObjectives().isEmpty()) {
            companyKeyResultDto.add(entity.getBusinessUnitObjectives().stream().map(businessUnitObjective -> linkTo(methodOn(BusinessUnitObjectiveController.class).findById(businessUnitObjective.getId())).withRel("businessUnitObjectives")).collect(Collectors.toList()));
        }
        if (entity.getBusinessUnitKeyResults() != null && !entity.getBusinessUnitKeyResults().isEmpty()) {
            companyKeyResultDto.add(entity.getCompanyKeyResultHistories().stream().map(businessUnitKeyResult -> linkTo(methodOn(BusinessUnitKeyResultController.class).findById(businessUnitKeyResult.getId())).withRel("businessUnitKeyResults")).collect(Collectors.toList()));
        }
        return companyKeyResultDto;
    }
}