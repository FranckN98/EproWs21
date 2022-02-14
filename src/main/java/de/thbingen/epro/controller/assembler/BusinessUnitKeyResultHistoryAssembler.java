package de.thbingen.epro.controller.assembler;

import de.thbingen.epro.controller.BusinessUnitKeyResultHistoryController;
import de.thbingen.epro.model.business.BusinessUnitKeyResultHistory;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultHistoryMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BusinessUnitKeyResultHistoryAssembler
        implements RepresentationModelAssembler<BusinessUnitKeyResultHistory, BusinessUnitKeyResultHistoryDto> {

    private final Class<BusinessUnitKeyResultHistoryController> controllerClass = BusinessUnitKeyResultHistoryController.class;

    private final BusinessUnitKeyResultHistoryMapper businessUnitKeyResultHistoryMapper;

    public BusinessUnitKeyResultHistoryAssembler(BusinessUnitKeyResultHistoryMapper businessUnitKeyResultHistoryMapper) {
        this.businessUnitKeyResultHistoryMapper = businessUnitKeyResultHistoryMapper;
    }

    @Override
    public BusinessUnitKeyResultHistoryDto toModel(BusinessUnitKeyResultHistory entity) {
        return businessUnitKeyResultHistoryMapper.businessUnitKeyResultHistoryToDto(entity)
                .add(linkTo(methodOn(controllerClass).getById(entity.getId())).withSelfRel());
        //TODO: .add(linkTo(methodOn(BusinessUnitKeyResultController.class).getById(entity.getCurrentBusinessUnitKeyResult().getId())).withRel("Test"));
    }
}
