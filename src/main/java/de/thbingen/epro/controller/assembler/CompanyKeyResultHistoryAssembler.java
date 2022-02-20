package de.thbingen.epro.controller.assembler;

import de.thbingen.epro.controller.CompanyKeyResultController;
import de.thbingen.epro.controller.CompanyKeyResultHistoryController;
import de.thbingen.epro.controller.CompanyObjectiveController;
import de.thbingen.epro.model.business.CompanyKeyResultHistory;
import de.thbingen.epro.model.dto.CompanyKeyResultHistoryDto;
import de.thbingen.epro.model.mapper.CompanyKeyResultHistoryMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CompanyKeyResultHistoryAssembler
        implements RepresentationModelAssembler<CompanyKeyResultHistory, CompanyKeyResultHistoryDto> {

    private final Class<CompanyKeyResultHistoryController> controllerClass = CompanyKeyResultHistoryController.class;

    private final CompanyKeyResultHistoryMapper companyKeyResultHistoryMapper;

    public CompanyKeyResultHistoryAssembler(CompanyKeyResultHistoryMapper companyKeyResultHistoryMapper) {
        this.companyKeyResultHistoryMapper = companyKeyResultHistoryMapper;
    }

    @Override
    public CompanyKeyResultHistoryDto toModel(CompanyKeyResultHistory entity) {
        return companyKeyResultHistoryMapper.companyKeyResultHistoryToDto(entity)
                .add(linkTo(methodOn(controllerClass).getById(entity.getId())).withSelfRel())
                .add(linkTo(methodOn(CompanyKeyResultController.class).findById(entity.getCompanyKeyResult().getId())).withRel("currentBusinessUnit"))
                .add(linkTo(methodOn(CompanyObjectiveController.class).findById(entity.getHistoricalCompanyKeyResult().getCompanyObjectiveId())).withRel("companyObjective"));
    }
}
