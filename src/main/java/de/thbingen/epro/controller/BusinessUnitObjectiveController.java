package de.thbingen.epro.controller;

import de.thbingen.epro.exception.InvalidDateRangeException;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultPostDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.entity.BusinessUnitObjective;
import de.thbingen.epro.service.BusinessUnitKeyResultService;
import de.thbingen.epro.service.BusinessUnitObjectiveService;
import de.thbingen.epro.service.CompanyKeyResultService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

/**
 * This Controller is responsible for everything under the /businessUnitObjectives endpoint
 */
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

    /**
     * Returns all {@link de.thbingen.epro.model.entity.BusinessUnitObjective}s of the requested Page, that have a
     * start Date that starts after the given start date and an end date that ends after the given end date
     *
     * @param pageable the parameters determining which page to return
     * @param start    the start date after which {@link de.thbingen.epro.model.entity.BusinessUnitObjective}s must start to be returned
     * @param end      the end date before which {@link de.thbingen.epro.model.entity.BusinessUnitObjective}s must end to be returned
     * @return the requested {@link de.thbingen.epro.model.entity.BusinessUnitObjective}s
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @PreAuthorize("hasAuthority('read')")
    public PagedModel<EntityModel<BusinessUnitObjectiveDto>> findAll(
            @PageableDefault Pageable pageable,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> end
    ) {
        LocalDate startDate = start.orElse(LocalDate.now().with(firstDayOfYear()));
        LocalDate endDate = end.orElse(LocalDate.now().with(lastDayOfYear()));
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException();
        }
        return pagedResourcesAssembler.toModel(
                businessUnitObjectiveService.getAllBusinessUnitObjectives(
                        pageable,
                        startDate,
                        endDate
                )
        );
    }

    /**
     * Returns the {@link de.thbingen.epro.model.entity.BusinessUnitObjective} with the given id
     *
     * @param id of the {@link de.thbingen.epro.model.entity.BusinessUnitObjective} to be returned
     * @return the {@link BusinessUnitObjective} with the given id
     */
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @PreAuthorize("hasAuthority('read')")
    public BusinessUnitObjectiveDto findById(@PathVariable Long id) {
        Optional<BusinessUnitObjectiveDto> result = businessUnitObjectiveService.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new EntityNotFoundException("No BusinessUnitObjective with this businessUnitObjectiveId exists");
    }

    /**
     * Update the {@link BusinessUnitObjective} with the given id from the values in the request body
     * @param id of the {@link BusinessUnitObjective} to be updated
     * @param businessUnitObjectiveDto the values to update the {@link BusinessUnitObjective} with
     * @return the newly updated {@link BusinessUnitObjective}
     */
    @PutMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or hasAuthority('change_own_BU_OKRs')")
    public ResponseEntity<BusinessUnitObjectiveDto> updateById(
            @PathVariable Long id,
            @RequestBody @Valid BusinessUnitObjectiveDto businessUnitObjectiveDto
    ) {
        if (!businessUnitObjectiveService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnitObjective with this businessUnitObjectiveId exists");
        }
        return ResponseEntity.ok(businessUnitObjectiveService.updateBusinessUnitObjective(id, businessUnitObjectiveDto));
    }

    /**
     * Delete the {@link BusinessUnitObjective} with the given id
     * @param id of the {@link BusinessUnitObjective} to be deleted
     * @return no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or hasAuthority('change_own_BU_OKRs')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!businessUnitObjectiveService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnitObjective with this businessUnitObjectiveId exists");
        }
        businessUnitObjectiveService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // region companyKeyResultReference

    /**
     * Creates a reference between the {@link BusinessUnitObjective} with the given businessUnitObjectiveId and the
     * {@link de.thbingen.epro.model.entity.CompanyKeyResult} with the given companyKeyResultId
     * @param businessUnitObjectiveId of the {@link BusinessUnitObjective} which is to be updated
     * @param companyKeyResultId of the {@link de.thbingen.epro.model.entity.CompanyKeyResult} which is to be updated
     * @return NoContent
     */
    @RequestMapping(
            value = "/{businessUnitObjectiveId}/companyKeyResultReference/{companyKeyResultId}",
            method = {RequestMethod.PUT, RequestMethod.POST}
    )
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or hasAuthority('change_own_BU_OKRs')")
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

    /**
     * Delete the reference to any {@link de.thbingen.epro.model.entity.CompanyKeyResult} from the {@link BusinessUnitObjective}
     * with the given id
     * @param businessUnitObjectiveId of the {@link BusinessUnitObjective} for which the reference is to be deleted
     * @return no content
     */
    @DeleteMapping("/{businessUnitObjectiveId}/companyKeyResultReference")
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or hasAuthority('change_own_BU_OKRs')")
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

    /**
     * Get all {@link de.thbingen.epro.model.entity.BusinessUnitKeyResult}s belonging to the {@link BusinessUnitObjective}
     * with the given id
     *
     * @param businessUnitObjectiveId of the {@link BusinessUnitObjective} that the returned {@link de.thbingen.epro.model.entity.BusinessUnitKeyResult}s should belong to
     * @param pageable the parameters determining which page to return
     * @return the requested page of {@link de.thbingen.epro.model.entity.BusinessUnitKeyResult}s
     */
    @GetMapping(
            value = "/{businessUnitObjectiveId}/keyResults",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('read')")
    public PagedModel<EntityModel<BusinessUnitKeyResultDto>> findAll(
            @PathVariable Long businessUnitObjectiveId,
            @PageableDefault Pageable pageable
    ) {
        return pagedResourcesAssemblerKeyResult.toModel(businessUnitKeyResultService.findAllByBusinessUnitObjectiveId(businessUnitObjectiveId, pageable));
    }

    /**
     * Adds a new {@link de.thbingen.epro.model.entity.BusinessUnitKeyResult} to the {@link BusinessUnitObjective}
     * with the given id
     *
     * @param businessUnitObjectiveId of the {@link BusinessUnitObjective} the {@link de.thbingen.epro.model.entity.BusinessUnitKeyResult} should be added to
     * @param newBusinessUnitKeyResultDto the {@link de.thbingen.epro.model.entity.BusinessUnitKeyResult} to be added
     * @return the newly added {@link de.thbingen.epro.model.entity.BusinessUnitKeyResult}
     */
    @PostMapping(
            value = "/{businessUnitObjectiveId}/keyResults",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or hasAuthority('change_own_BU_OKRs')")
    public ResponseEntity<BusinessUnitKeyResultDto> addNewKeyResult(
            @PathVariable Long businessUnitObjectiveId,
            @RequestBody @Valid BusinessUnitKeyResultPostDto newBusinessUnitKeyResultDto
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
