package de.thbingen.epro.model.assembler;

import de.thbingen.epro.controller.CompanyKeyResultController;
import de.thbingen.epro.controller.CompanyObjectiveController;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.model.entity.CompanyObjective;
import de.thbingen.epro.model.mapper.CompanyObjectiveMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CompanyObjectiveAssembler implements RepresentationModelAssembler<CompanyObjective, CompanyObjectiveDto> {

    private final Class<CompanyObjectiveController> controllerClass = CompanyObjectiveController.class;

    private final CompanyObjectiveMapper companyObjectiveMapper;
    private final AnnotationLinkRelationProvider annotationLinkRelationProvider;

    public CompanyObjectiveAssembler(CompanyObjectiveMapper companyObjectiveMapper, AnnotationLinkRelationProvider annotationLinkRelationProvider) {
        this.companyObjectiveMapper = companyObjectiveMapper;
        this.annotationLinkRelationProvider = annotationLinkRelationProvider;
    }

    @Override
    public CompanyObjectiveDto toModel(CompanyObjective entity) {
        CompanyObjectiveDto companyObjectiveDto = companyObjectiveMapper.companyObjectiveToDto(entity)
                .add(linkTo(methodOn(controllerClass).findById(entity.getId())).withSelfRel());

        if (entity.getCompanyKeyResults() != null && !entity.getCompanyKeyResults().isEmpty()) {
            companyObjectiveDto.add(
                    linkTo(methodOn(CompanyObjectiveController.class).findAllCompanyKeyResultsByCompanyObjectiveId(null, entity.getId()))
                            .withRel(annotationLinkRelationProvider.getCollectionResourceRelFor(CompanyKeyResultDto.class))
            );
        }

        return companyObjectiveDto;
    }
}
