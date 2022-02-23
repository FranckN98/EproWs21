package de.thbingen.epro.controller.businessunitobjective;

import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.service.BusinessUnitKeyResultService;
import de.thbingen.epro.service.BusinessUnitObjectiveService;
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

@RestController
@RequestMapping("/businessUnitObjectives/{id}/keyResults")
public class BusinessUnitKeyResultByBusinessUnitObjectiveController {


    private final BusinessUnitKeyResultService businessUnitKeyResultService;
    private final BusinessUnitObjectiveService businessUnitObjectiveService;
    private final PagedResourcesAssembler<BusinessUnitKeyResultDto> pagedResourcesAssembler;

    public BusinessUnitKeyResultByBusinessUnitObjectiveController(BusinessUnitKeyResultService businessUnitKeyResultService, BusinessUnitObjectiveService businessUnitObjectiveService, PagedResourcesAssembler<BusinessUnitKeyResultDto> pagedResourcesAssembler) {
        this.businessUnitKeyResultService = businessUnitKeyResultService;
        this.businessUnitObjectiveService = businessUnitObjectiveService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping(
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.ALL_VALUE
    )
    public PagedModel<EntityModel<BusinessUnitKeyResultDto>> findAll(
            @PathVariable Long id,
            @PageableDefault Pageable pageable
    ) {
        return pagedResourcesAssembler.toModel(businessUnitKeyResultService.getAllByBusinessUnitObjectiveId(id, pageable));
    }

    @PostMapping(
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BusinessUnitKeyResultDto> addNewKeyResult(
            @PathVariable Long id,
            @RequestBody @Valid BusinessUnitKeyResultDto newBusinessUnitKeyResultDto
    ) {
        if (!businessUnitObjectiveService.existsById(id))
            throw new EntityNotFoundException("No BusinessUnitObjective with this ID exists.");

        BusinessUnitKeyResultDto businessUnitKeyResultDto =
                businessUnitKeyResultService.insertBusinessUnitKeyResultWithObjective(
                        newBusinessUnitKeyResultDto,
                        id
                );

        return ResponseEntity.created(businessUnitKeyResultDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(businessUnitKeyResultDto);
    }
}
