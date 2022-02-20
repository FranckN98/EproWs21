package de.thbingen.epro.controller.assembler;

import de.thbingen.epro.controller.CompanyObjectiveController;
import de.thbingen.epro.controller.businessunitobjective.BusinessUnitObjectiveController;
import de.thbingen.epro.model.business.BusinessUnitObjective;
import de.thbingen.epro.model.business.CompanyObjective;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.model.mapper.BusinessUnitObjectiveMapper;
import de.thbingen.epro.model.mapper.CompanyObjectiveMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BusinessUnitObjectiveAssembler implements RepresentationModelAssembler<BusinessUnitObjective, BusinessUnitObjectiveDto> {

    private final Class<BusinessUnitObjectiveController> controllerClass = BusinessUnitObjectiveController.class;

    private final BusinessUnitObjectiveMapper businessUnitObjectiveMapper;

    public BusinessUnitObjectiveAssembler(BusinessUnitObjectiveMapper businessUnitObjectiveMapper) {
        this.businessUnitObjectiveMapper = businessUnitObjectiveMapper;
    }

    @Override
    public BusinessUnitObjectiveDto toModel(BusinessUnitObjective entity) {
        return businessUnitObjectiveMapper.businessUnitObjectiveToDto(entity)
                .add(linkTo(methodOn(controllerClass).findById(entity.getId())).withSelfRel());
    }




}
