package de.thbingen.epro.model.assembler;

import de.thbingen.epro.controller.*;
import de.thbingen.epro.model.dto.*;
import de.thbingen.epro.model.entity.CompanyKeyResult;
import de.thbingen.epro.model.mapper.CompanyKeyResultMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CompanyKeyResultAssembler implements RepresentationModelAssembler<CompanyKeyResult, CompanyKeyResultDto> {

    private final CompanyKeyResultMapper companyKeyResultMapper;
    private final AnnotationLinkRelationProvider annotationLinkRelationProvider;

    public CompanyKeyResultAssembler(CompanyKeyResultMapper companyKeyResultMapper, AnnotationLinkRelationProvider annotationLinkRelationProvider) {
        this.companyKeyResultMapper = companyKeyResultMapper;
        this.annotationLinkRelationProvider = annotationLinkRelationProvider;
    }

    @Override
    public CompanyKeyResultDto toModel(CompanyKeyResult entity) {
        CompanyKeyResultDto companyKeyResultDto = companyKeyResultMapper.companyKeyResultToDto(entity)
                .add(linkTo(methodOn(CompanyKeyResultController.class).findById(entity.getId())).withSelfRel());

        if (entity.getCompanyObjective() != null) {
            companyKeyResultDto.add(
                    linkTo(methodOn(CompanyObjectiveController.class).findById(entity.getCompanyObjective().getId()))
                            .withRel(annotationLinkRelationProvider.getItemResourceRelFor(CompanyObjectiveDto.class))
            );
        }
        if (entity.getCompanyKeyResultHistories() != null && !entity.getCompanyKeyResultHistories().isEmpty()) {
            companyKeyResultDto.add(
                    linkTo(methodOn(CompanyKeyResultHistoryController.class).getAll(null))
                            .withRel(annotationLinkRelationProvider.getCollectionResourceRelFor(CompanyKeyResultHistoryDto.class))
            );
        }
        if (entity.getBusinessUnitObjectives() != null && !entity.getBusinessUnitObjectives().isEmpty()) {
            companyKeyResultDto.add(
                    entity.getBusinessUnitObjectives().stream().map(businessUnitObjective ->
                            linkTo(methodOn(BusinessUnitObjectiveController.class).findById(businessUnitObjective.getId()))
                                    .withRel(annotationLinkRelationProvider.getCollectionResourceRelFor(BusinessUnitObjectiveDto.class)))
                            .collect(Collectors.toList())
            );
        }
        if (entity.getBusinessUnitKeyResults() != null && !entity.getBusinessUnitKeyResults().isEmpty()) {
            companyKeyResultDto.add(
                    entity.getCompanyKeyResultHistories().stream().map(businessUnitKeyResult ->
                            linkTo(methodOn(BusinessUnitKeyResultController.class).findById(businessUnitKeyResult.getId()))
                                    .withRel(annotationLinkRelationProvider.getCollectionResourceRelFor(BusinessUnitKeyResultDto.class)))
                            .collect(Collectors.toList())
            );
        }

        return companyKeyResultDto;
    }
}