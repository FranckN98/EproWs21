package de.thbingen.epro.controller.assembler;

import de.thbingen.epro.controller.CompanyKeyResultController;
import de.thbingen.epro.model.business.CompanyKeyResult;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.mapper.CompanyKeyResultMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

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
        return companyKeyResultDto;
    }
}

/*
@Component
public class BusinessUnitAssembler implements RepresentationModelAssembler<BusinessUnit, BusinessUnitDto> {

    private final BusinessUnitMapper businessUnitMapper;

    public BusinessUnitAssembler(BusinessUnitMapper businessUnitMapper) {
        this.businessUnitMapper = businessUnitMapper;
    }

    @Override
    public BusinessUnitDto toModel(BusinessUnit entity) {
        BusinessUnitDto businessUnitDto = businessUnitMapper.businessUnitToDto(entity)
                .add(linkTo(methodOn(BusinessUnitController.class).findById(entity.getId())).withSelfRel());
        if(entity.getBusinessUnitObjectives() != null && !entity.getBusinessUnitObjectives().isEmpty()) {
            businessUnitDto.add(linkTo(methodOn(BusinessUnitObjectiveByBusinessUnitController.class)
                    .getAllBusinessUnitObjectives(null, entity.getId())).withRel("businessUnitObjectives"));
        }
        if(entity.getBusinessUnitObjectives() != null && !entity.getOkrUsers().isEmpty()) {
            businessUnitDto.add(linkTo(methodOn(BusinessUnitOkrUserController.class)
                    .getAllOkrUsers(null, entity.getId())).withRel("users"));
        }
        return businessUnitDto;
    }
}
 */