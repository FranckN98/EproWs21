package de.thbingen.epro.model.assembler;

import de.thbingen.epro.controller.BusinessUnitKeyResultController;
import de.thbingen.epro.controller.BusinessUnitKeyResultHistoryController;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.model.entity.BusinessUnitKeyResultHistory;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultHistoryMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BusinessUnitKeyResultHistoryAssembler
        implements RepresentationModelAssembler<BusinessUnitKeyResultHistory, BusinessUnitKeyResultHistoryDto> {

    private final Class<BusinessUnitKeyResultHistoryController> controllerClass = BusinessUnitKeyResultHistoryController.class;

    private final BusinessUnitKeyResultHistoryMapper businessUnitKeyResultHistoryMapper;
    private final AnnotationLinkRelationProvider annotationLinkRelationProvider;

    public BusinessUnitKeyResultHistoryAssembler(BusinessUnitKeyResultHistoryMapper businessUnitKeyResultHistoryMapper, AnnotationLinkRelationProvider annotationLinkRelationProvider) {
        this.businessUnitKeyResultHistoryMapper = businessUnitKeyResultHistoryMapper;
        this.annotationLinkRelationProvider = annotationLinkRelationProvider;
    }

    @Override
    public BusinessUnitKeyResultHistoryDto toModel(BusinessUnitKeyResultHistory entity) {
        BusinessUnitKeyResultHistoryDto businessUnitKeyResultHistory = businessUnitKeyResultHistoryMapper.businessUnitKeyResultHistoryToDto(entity)
                .add(linkTo(methodOn(controllerClass).getById(entity.getId())).withSelfRel());

        businessUnitKeyResultHistory.add(
                linkTo(methodOn(BusinessUnitKeyResultController.class).findById(entity.getCurrentBusinessUnitKeyResult().getId()))
                        .withRel(annotationLinkRelationProvider.getItemResourceRelFor(BusinessUnitKeyResultDto.class))
        );

        return businessUnitKeyResultHistory;
    }
}
