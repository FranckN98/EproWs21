package de.thbingen.epro.model.assembler;

import de.thbingen.epro.controller.BusinessUnitController;
import de.thbingen.epro.controller.BusinessUnitKeyResultController;
import de.thbingen.epro.controller.BusinessUnitObjectiveController;
import de.thbingen.epro.controller.CompanyKeyResultController;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.entity.BusinessUnitObjective;
import de.thbingen.epro.model.mapper.BusinessUnitObjectiveMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BusinessUnitObjectiveAssembler implements RepresentationModelAssembler<BusinessUnitObjective, BusinessUnitObjectiveDto> {

    private final BusinessUnitObjectiveMapper mapper;
    private final AnnotationLinkRelationProvider annotationLinkRelationProvider;

    public BusinessUnitObjectiveAssembler(BusinessUnitObjectiveMapper mapper, AnnotationLinkRelationProvider annotationLinkRelationProvider) {
        this.mapper = mapper;
        this.annotationLinkRelationProvider = annotationLinkRelationProvider;
    }

    @Override
    public BusinessUnitObjectiveDto toModel(BusinessUnitObjective entity) {
        BusinessUnitObjectiveDto businessUnitObjectiveDto = mapper.businessUnitObjectiveToDto(entity)
                .add(linkTo(methodOn(BusinessUnitObjectiveController.class).findById(entity.getId())).withSelfRel())
                .add(
                        linkTo(methodOn(BusinessUnitController.class).findById(entity.getBusinessUnit().getId()))
                                .withRel(annotationLinkRelationProvider.getItemResourceRelFor(BusinessUnitDto.class))
                );

        if (entity.getBusinessUnitKeyResults() != null && !entity.getBusinessUnitKeyResults().isEmpty()) {
            businessUnitObjectiveDto.add(
                    entity.getBusinessUnitKeyResults()
                            .stream()
                            .map(businessUnitKeyResult ->
                                    linkTo(methodOn(BusinessUnitKeyResultController.class)
                                            .findById(businessUnitKeyResult.getId())
                                    ).withRel(annotationLinkRelationProvider.getCollectionResourceRelFor(BusinessUnitKeyResultDto.class))
                            )
                            .collect(Collectors.toList()));
        }

        if (entity.getCompanyKeyResult() != null) {
            businessUnitObjectiveDto.add(
                    linkTo(methodOn(CompanyKeyResultController.class).findById(entity.getCompanyKeyResult().getId()))
                            .withRel(annotationLinkRelationProvider.getItemResourceRelFor(CompanyKeyResultDto.class))
            );
        }
        return businessUnitObjectiveDto;
    }
}
