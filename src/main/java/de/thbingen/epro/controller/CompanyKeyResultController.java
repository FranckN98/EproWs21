package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyKeyResultHistoryDto;
import de.thbingen.epro.model.dto.CompanyKeyResultUpdateDto;
import de.thbingen.epro.service.CompanyKeyResultHistoryService;
import de.thbingen.epro.service.CompanyKeyResultService;
import org.springframework.data.domain.Page;
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
 * This Controller is responsible for everything under the /companyKeyResults endpoint
 */
@RestController
@RequestMapping("/companyKeyResults")
public class CompanyKeyResultController {

    private final CompanyKeyResultService companyKeyResultService;
    private final PagedResourcesAssembler<CompanyKeyResultDto> pagedResourcesAssembler;
    private final PagedResourcesAssembler<CompanyKeyResultHistoryDto> companyKeyResultHistoryDtoPagedResourcesAssembler;
    private final CompanyKeyResultHistoryService companyKeyResultHistoryService;

    public CompanyKeyResultController(CompanyKeyResultService companyKeyResultService, PagedResourcesAssembler<CompanyKeyResultDto> pagedResourcesAssembler, PagedResourcesAssembler<CompanyKeyResultHistoryDto> companyKeyResultHistoryDtoPagedResourcesAssembler, CompanyKeyResultHistoryService companyKeyResultHistoryService) {
        this.companyKeyResultService = companyKeyResultService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.companyKeyResultHistoryDtoPagedResourcesAssembler = companyKeyResultHistoryDtoPagedResourcesAssembler;
        this.companyKeyResultHistoryService = companyKeyResultHistoryService;
    }

    /**
     * Returns all {@link de.thbingen.epro.model.entity.CompanyKeyResult}s
     * @param pageable the parameters determining which page to return
     * @return all {@link de.thbingen.epro.model.entity.CompanyKeyResult}s of the requested page
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @PreAuthorize("hasAuthority('read')")
    public PagedModel<EntityModel<CompanyKeyResultDto>> findAll(
            @PageableDefault Pageable pageable
    ) {
        Page<CompanyKeyResultDto> allCompanyKeyResults = companyKeyResultService.findAllCompanyKeyResults(pageable);
        return pagedResourcesAssembler.toModel(allCompanyKeyResults);
    }

    /**
     * Returns the {@link de.thbingen.epro.model.entity.CompanyKeyResult} with the given id
     * @param id of the {@link de.thbingen.epro.model.entity.CompanyKeyResult} to be returned
     * @return the {@link de.thbingen.epro.model.entity.CompanyKeyResult} with the given id
     */
    @GetMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('read')")
    public CompanyKeyResultDto findById(@PathVariable Long id) {
        Optional<CompanyKeyResultDto> result = companyKeyResultService.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new EntityNotFoundException("No CompanyKeyResult with this id exists");
    }

    /**
     * Updates the {@link de.thbingen.epro.model.entity.CompanyKeyResult} with the given id with the values from the
     * request body
     * @param id of the {@link de.thbingen.epro.model.entity.CompanyKeyResult} to be updated
     * @param companyKeyResultDto the new values of the {@link de.thbingen.epro.model.entity.CompanyKeyResult}
     * @return the newly updated {@link de.thbingen.epro.model.entity.CompanyKeyResult}
     */
    @PutMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('change_CO_OKRs')")
    public ResponseEntity<CompanyKeyResultDto> updateById(
            @PathVariable Long id,
            @RequestBody @Valid CompanyKeyResultUpdateDto companyKeyResultDto
    ) {
        if (!companyKeyResultService.existsById(id)) {
            throw new EntityNotFoundException("No CompanyKeyResult with this id exists");
        }
        return ResponseEntity.ok(companyKeyResultService.updateCompanyKeyResult(id, companyKeyResultDto));
    }

    /**
     * Deletes the {@link de.thbingen.epro.model.entity.CompanyKeyResult} with the given id
     * @param id of the {@link de.thbingen.epro.model.entity.CompanyKeyResult} to be deleted
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('change_CO_OKRs')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!companyKeyResultService.existsById(id)) {
            throw new EntityNotFoundException("No CompanyKeyResult with this id exists");
        }
        companyKeyResultService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get the requested page of history for the {@link de.thbingen.epro.model.entity.CompanyKeyResult} with the given id
     * @param pageable the parameters determining which page to return
     * @param id of the {@link de.thbingen.epro.model.entity.CompanyKeyResult} for which the history shall be returned
     * @return the requested page of history for the {@link de.thbingen.epro.model.entity.CompanyKeyResult} with the given id
     */
    @GetMapping(value = "/{id}/history", produces = MediaTypes.HAL_JSON_VALUE)
    @PreAuthorize("hasAuthority('read')")
    public PagedModel<EntityModel<CompanyKeyResultHistoryDto>> getHistory(
            @PageableDefault Pageable pageable,
            @PathVariable Long id
    ) {
        if (!companyKeyResultService.existsById(id)) {
            throw new EntityNotFoundException("No CompanyKeyResult with this id exists");
        }
        return companyKeyResultHistoryDtoPagedResourcesAssembler.toModel(
                companyKeyResultHistoryService.findAllByCompanyKeyResultId(id, pageable)
        );
    }
}
