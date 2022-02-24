package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.service.BusinessUnitKeyResultService;
import de.thbingen.epro.service.BusinessUnitObjectiveService;
import de.thbingen.epro.service.CompanyKeyResultService;
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
@RequestMapping("/businessUnitObjectives")
public class BusinessUnitObjectiveController {

    private final BusinessUnitObjectiveService businessUnitObjectiveService;
    private final CompanyKeyResultService companyKeyResultService;
    private final PagedResourcesAssembler<BusinessUnitObjectiveDto> pagedResourcesAssembler;
    private final BusinessUnitKeyResultService businessUnitKeyResultService;
    private final PagedResourcesAssembler<BusinessUnitKeyResultDto> pagedResourcesAssemblerKeyResult;

    public BusinessUnitObjectiveController(BusinessUnitObjectiveService businessUnitObjectiveService, CompanyKeyResultService companyKeyResultService, PagedResourcesAssembler<BusinessUnitObjectiveDto> pagedResourcesAssembler, BusinessUnitKeyResultService businessUnitKeyResultService, PagedResourcesAssembler<BusinessUnitKeyResultDto> pagedResourcesAssemblerKeyResult) {
        this.businessUnitObjectiveService = businessUnitObjectiveService;
        this.companyKeyResultService = companyKeyResultService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.businessUnitKeyResultService = businessUnitKeyResultService;
        this.pagedResourcesAssemblerKeyResult = pagedResourcesAssemblerKeyResult;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public PagedModel<EntityModel<BusinessUnitObjectiveDto>> findAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(businessUnitObjectiveService.getAllBusinessUnitObjectives(pageable));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public BusinessUnitObjectiveDto findById(@PathVariable Long id) {
        Optional<BusinessUnitObjectiveDto> result = businessUnitObjectiveService.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new EntityNotFoundException("No BusinessUnitObjective with this businessUnitObjectiveId exists");
    }

    // TODO: I should be deleted as I cant possibly work anymore
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<BusinessUnitObjectiveDto> addNew(@RequestBody @Valid BusinessUnitObjectiveDto newBusinessUnitObjectiveDto) {
        BusinessUnitObjectiveDto businessUnitObjectiveDto = businessUnitObjectiveService.insertBusinessUnitObjective(newBusinessUnitObjectiveDto);
        return ResponseEntity.created(businessUnitObjectiveDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(businessUnitObjectiveDto);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<BusinessUnitObjectiveDto> updateById(
            @PathVariable Long id,
            @RequestBody @Valid BusinessUnitObjectiveDto businessUnitObjectiveDto
    ) {
        if (!businessUnitObjectiveService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnitObjective with this businessUnitObjectiveId exists");
        }
        return ResponseEntity.ok(businessUnitObjectiveService.updateBusinessUnitObjective(id, businessUnitObjectiveDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!businessUnitObjectiveService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnitObjective with this businessUnitObjectiveId exists");
        }
        businessUnitObjectiveService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // region companyKeyResultReference

    @RequestMapping(
            value = "/{businessUnitObjectiveId}/companyKeyResultReference/{companyKeyResultId}",
            method = {RequestMethod.PUT, RequestMethod.POST}
    )
    public ResponseEntity<Void> referenceCompanyKeyResult(
            @PathVariable Long businessUnitObjectiveId,
            @PathVariable Long companyKeyResultId
    ) {
        if (!businessUnitObjectiveService.existsById(businessUnitObjectiveId)) {
            throw new EntityNotFoundException("No BusinessUnitObjective with this businessUnitObjectiveId exists");
        }
        if (!companyKeyResultService.existsById(companyKeyResultId)) {
            throw new EntityNotFoundException("No CompanyKeyResult with this businessUnitObjectiveId exists");
        }
        if (businessUnitObjectiveService.referenceCompanyKeyResult(businessUnitObjectiveId, companyKeyResultId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{businessUnitObjectiveId}/companyKeyResultReference")
    public ResponseEntity<Void> deleteCompanyKeyResultReference(
            @PathVariable Long businessUnitObjectiveId
    ) {
        if (!businessUnitObjectiveService.existsById(businessUnitObjectiveId)) {
            throw new EntityNotFoundException("No BusinessUnitObjective with this businessUnitObjectiveId exists");
        }
        if (businessUnitObjectiveService.deleteCompanyKeyResultReference(businessUnitObjectiveId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    // endregion

    // region keyResults

    @GetMapping(
            value = "/{businessUnitObjectiveId}/keyResults",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.ALL_VALUE
    )
    public PagedModel<EntityModel<BusinessUnitKeyResultDto>> findAll(
            @PathVariable Long businessUnitObjectiveId,
            @PageableDefault Pageable pageable
    ) {
        return pagedResourcesAssemblerKeyResult.toModel(businessUnitKeyResultService.getAllByBusinessUnitObjectiveId(businessUnitObjectiveId, pageable));
    }

    @PostMapping(
            value = "/{businessUnitObjectiveId}/keyResults",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BusinessUnitKeyResultDto> addNewKeyResult(
            @PathVariable Long businessUnitObjectiveId,
            @RequestBody @Valid BusinessUnitKeyResultDto newBusinessUnitKeyResultDto
    ) {
        if (!businessUnitObjectiveService.existsById(businessUnitObjectiveId))
            throw new EntityNotFoundException("No BusinessUnitObjective with this ID exists.");

        BusinessUnitKeyResultDto businessUnitKeyResultDto =
                businessUnitKeyResultService.insertBusinessUnitKeyResultWithObjective(
                        newBusinessUnitKeyResultDto,
                        businessUnitObjectiveId
                );

        return ResponseEntity.created(businessUnitKeyResultDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(businessUnitKeyResultDto);
    }

    // endregion
}
