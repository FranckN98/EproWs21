package de.thbingen.epro.controller.businessunit;

import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.service.BusinessUnitObjectiveService;
import de.thbingen.epro.service.BusinessUnitService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/businessUnits/{id}/objectives")
public class BusinessUnitObjectiveByBusinessUnitController {

    private final BusinessUnitService businessUnitService;
    private final BusinessUnitObjectiveService businessUnitObjectiveService;
    private final PagedResourcesAssembler<BusinessUnitObjectiveDto> pagedResourcesAssembler;

    public BusinessUnitObjectiveByBusinessUnitController(BusinessUnitService businessUnitService, BusinessUnitObjectiveService businessUnitObjectiveService, PagedResourcesAssembler<BusinessUnitObjectiveDto> pagedResourcesAssembler) {
        this.businessUnitService = businessUnitService;
        this.businessUnitObjectiveService = businessUnitObjectiveService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public PagedModel<EntityModel<BusinessUnitObjectiveDto>> getAllBusinessUnitObjectives(
            @PageableDefault Pageable pageable,
            @PathVariable Long id
    ) {
        return pagedResourcesAssembler.toModel(businessUnitObjectiveService.getAllByBusinessUnitId(id, pageable));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessUnitObjectiveDto> addNewBusinessUnitObjective(
            @PathVariable Long id,
            @RequestBody @Valid BusinessUnitObjectiveDto newBusinessUnitObjectiveDto
    ) {
        Optional<BusinessUnitDto> businessUnit = businessUnitService.findById(id);

        if(businessUnit.isPresent()) {
            BusinessUnitObjectiveDto businessUnitObjectiveDto = businessUnitObjectiveService.saveBusinessUnitObjectiveWithBusinessUnit(newBusinessUnitObjectiveDto, businessUnit.get());
            return ResponseEntity.created(businessUnitObjectiveDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(businessUnitObjectiveDto);
        }

        throw new EntityNotFoundException("No BusinessUnit with the given ID exists");
    }
}
