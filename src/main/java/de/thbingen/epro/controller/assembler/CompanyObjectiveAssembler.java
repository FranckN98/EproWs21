package de.thbingen.epro.controller.assembler;

import de.thbingen.epro.controller.CompanyObjectiveController;
import de.thbingen.epro.model.business.CompanyObjective;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.model.mapper.CompanyObjectiveMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CompanyObjectiveAssembler implements RepresentationModelAssembler<CompanyObjective, CompanyObjectiveDto> {

    private final Class<CompanyObjectiveController> controllerClass = CompanyObjectiveController.class;

    private final CompanyObjectiveMapper companyObjectiveMapper;

    public CompanyObjectiveAssembler(CompanyObjectiveMapper companyObjectiveMapper) {
        this.companyObjectiveMapper = companyObjectiveMapper;
    }

    @Override
    public CompanyObjectiveDto toModel(CompanyObjective entity) {
        return companyObjectiveMapper.companyObjectiveToDto(entity)
                .add(linkTo(methodOn(controllerClass).findById(entity.getId())).withSelfRel());
    }
}
