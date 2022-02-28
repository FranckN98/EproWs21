package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultUpdateDto;
import de.thbingen.epro.model.entity.BusinessUnitKeyResult;
import de.thbingen.epro.model.entity.CompanyKeyResult;
import de.thbingen.epro.service.BusinessUnitKeyResultHistoryService;
import de.thbingen.epro.service.BusinessUnitKeyResultService;
import de.thbingen.epro.service.CompanyKeyResultService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

/**
 * This controller is responsible for the /businessUnitKeyResults endpoint
 */
@RestController
@RequestMapping("/businessUnitKeyResults")
public class BusinessUnitKeyResultController {

    private final BusinessUnitKeyResultService businessUnitKeyResultService;
    private final BusinessUnitKeyResultHistoryService businessUnitKeyResultHistoryService;
    private final PagedResourcesAssembler<BusinessUnitKeyResultDto> pagedResourcesAssembler;
    private final PagedResourcesAssembler<BusinessUnitKeyResultHistoryDto> businessUnitKeyResultHistoryDtoPagedResourcesAssembler;
    private final CompanyKeyResultService companyKeyResultService;

    public BusinessUnitKeyResultController(BusinessUnitKeyResultService businessUnitKeyResultService, BusinessUnitKeyResultHistoryService businessUnitKeyResultHistoryService, PagedResourcesAssembler<BusinessUnitKeyResultDto> pagedResourcesAssembler, PagedResourcesAssembler<BusinessUnitKeyResultHistoryDto> businessUnitKeyResultHistoryDtoPagedResourcesAssembler, CompanyKeyResultService companyKeyResultService) {
        this.businessUnitKeyResultService = businessUnitKeyResultService;
        this.businessUnitKeyResultHistoryService = businessUnitKeyResultHistoryService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.businessUnitKeyResultHistoryDtoPagedResourcesAssembler = businessUnitKeyResultHistoryDtoPagedResourcesAssembler;
        this.companyKeyResultService = companyKeyResultService;
    }

    /**
     * Returns all {@link de.thbingen.epro.model.entity.BusinessUnitKeyResult}s of the requested Page
     *
     * @param pageable the parameters, which determine which page to return
     * @return The requested Page of BusinessUnitKeyResults
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @PreAuthorize("hasAuthority('read')")
    public PagedModel<EntityModel<BusinessUnitKeyResultDto>> findAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(businessUnitKeyResultService.findAllBusinessUnitKeyResults(pageable));
    }

    /**
     * Returns the {@link BusinessUnitKeyResult} with the given id
     *
     * @param id of the BusinessUnitKeyResult which should be returned
     * @return the BusinessUnitKeyResult with the given id
     */
    @GetMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('read')")
    public BusinessUnitKeyResultDto findById(@PathVariable Long id) {
        Optional<BusinessUnitKeyResultDto> result = businessUnitKeyResultService.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new EntityNotFoundException("No BusinessUnitKeyResult with this id exists");
    }

    /**
     * Updates the {@link BusinessUnitKeyResult} with the given id, with the values contained in the request body
     *
     * @param id                       of the BusinessUnitKeyResult to be updated
     * @param businessUnitKeyResultDto The new values for the BusinessUnitKeyResult
     * @return the BusinessUnitKeyResult with its new values
     */
    @PutMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or hasAuthority('change_own_BU_OKRs')")
    public ResponseEntity<BusinessUnitKeyResultDto> updateById(
            @PathVariable Long id,
            @RequestBody @Valid BusinessUnitKeyResultUpdateDto businessUnitKeyResultDto
    ) {
        if (!businessUnitKeyResultService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnitKeyResult with this id exists");
        }
        return ResponseEntity.ok(businessUnitKeyResultService.updateBusinessUnitKeyResult(id, businessUnitKeyResultDto));
    }

    /**
     * Deletes the {@link BusinessUnitKeyResult} with the given id
     *
     * @param id of the BusinessUnitKeyResult to be deleted
     * @return NoContent
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or hasAuthority('change_own_BU_OKRs')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!businessUnitKeyResultService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnitKeyResult with this id exists");
        }
        businessUnitKeyResultService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Returns the complete history belonging to the {@link BusinessUnitKeyResult} with the given id
     *
     * @param pageable the parameters, determining which page to return
     * @param id       the id of the BusinessUnitKeyResult of which the history should be returned
     * @return the complete history
     */
    @GetMapping(value = "/{id}/history", produces = MediaTypes.HAL_JSON_VALUE)
    @PreAuthorize("hasAuthority('read')")
    public PagedModel<EntityModel<BusinessUnitKeyResultHistoryDto>> getHistory(
            @PageableDefault Pageable pageable,
            @PathVariable Long id
    ) {
        if (!businessUnitKeyResultService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnitKeyResult with this id exists");
        }
        return businessUnitKeyResultHistoryDtoPagedResourcesAssembler.toModel(
                businessUnitKeyResultHistoryService.findAllByBusinessUnitKeyResultId(id, pageable)
        );
    }

    /**
     * Adds a reference between {@link BusinessUnitKeyResult} and {@link CompanyKeyResult}
     * @param businessUnitKeyResultId The id of the BusinessUnitKeyResult which shall be linked to a CompanyKeyResult
     * @param companyKeyResultId of the CompanyKeyResult that shall be linked to
     * @return No Content
     */
    @RequestMapping(
            value = "/{businessUnitKeyResultId}/companyKeyResultReference/{companyKeyResultId}",
            method = {RequestMethod.PUT, RequestMethod.POST}
    )
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or hasAuthority('change_own_BU_OKRs')")
    public ResponseEntity<Void> referenceCompanyKeyResult(
            @PathVariable Long businessUnitKeyResultId,
            @PathVariable Long companyKeyResultId
    ) {
        if (!businessUnitKeyResultService.existsById(businessUnitKeyResultId)) {
            throw new EntityNotFoundException("No BusinessUnitKeyResult with this id exists");
        }
        if (!companyKeyResultService.existsById(companyKeyResultId)) {
            throw new EntityNotFoundException("No CompanyKeyResult with this id exists");
        }
        if (businessUnitKeyResultService.referenceCompanyKeyResult(businessUnitKeyResultId, companyKeyResultId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Deletes the link of the {@link BusinessUnitKeyResult} with the given {@code id} to any {@link CompanyKeyResult}
     * @param businessUnitKeyResultId of the BusinessUnitKeyResult for which the link shall be deleted
     * @return No Content
     */
    @DeleteMapping("/{businessUnitKeyResultId}/companyKeyResultReference")
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or hasAuthority('change_own_BU_OKRs')")
    public ResponseEntity<Void> deleteCompanyKeyResultReference(
            @PathVariable Long businessUnitKeyResultId
    ) {
        if (!businessUnitKeyResultService.existsById(businessUnitKeyResultId)) {
            throw new EntityNotFoundException("No BusinessUnitKeyResult with this id exists");
        }
        if (businessUnitKeyResultService.deleteCompanyKeyResultReference(businessUnitKeyResultId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
}
