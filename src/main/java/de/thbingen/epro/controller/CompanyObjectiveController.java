package de.thbingen.epro.controller;

import de.thbingen.epro.exception.InvalidDateRangeException;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyKeyResultPostDto;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.model.entity.BusinessUnitObjective;
import de.thbingen.epro.service.CompanyKeyResultService;
import de.thbingen.epro.service.CompanyObjectiveService;
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
 * This controller is responsible for everything under the /companyObjectives endpoint
 */
@RestController
@RequestMapping("/companyObjectives")
public class CompanyObjectiveController {

    private final CompanyKeyResultService companyKeyResultService;
    private final CompanyObjectiveService companyObjectiveService;
    private final PagedResourcesAssembler<CompanyObjectiveDto> pagedResourcesAssembler;
    private final PagedResourcesAssembler<CompanyKeyResultDto> companyKeyResultDtoPagedResourcesAssembler;

    public CompanyObjectiveController(CompanyObjectiveService companyObjectiveService, CompanyKeyResultService companyKeyResultService, PagedResourcesAssembler<CompanyObjectiveDto> pagedResourcesAssembler, PagedResourcesAssembler<CompanyKeyResultDto> companyKeyResultDtoPagedResourcesAssembler) {
        this.companyObjectiveService = companyObjectiveService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.companyKeyResultService = companyKeyResultService;
        this.companyKeyResultDtoPagedResourcesAssembler = companyKeyResultDtoPagedResourcesAssembler;
    }

    /**
     * Returns all {@link de.thbingen.epro.model.entity.CompanyObjective}s of the requested Page, that have a
     * start Date that starts after the given start date and an end date that ends after the given end date
     *
     * @param pageable the parameters determining which page to return
     * @param start    the start date after which {@link de.thbingen.epro.model.entity.CompanyObjective}s must start to be returned
     * @param end      the end date before which {@link de.thbingen.epro.model.entity.CompanyObjective}s must end to be returned
     * @return the requested {@link de.thbingen.epro.model.entity.CompanyObjective}s
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @PreAuthorize("hasAuthority('read')")
    public PagedModel<EntityModel<CompanyObjectiveDto>> findAll(
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
                companyObjectiveService.getAllCompanyObjectives(
                        pageable,
                        startDate,
                        endDate
                )
        );
    }

    /**
     * Returns the {@link de.thbingen.epro.model.entity.CompanyObjective} with the given id
     *
     * @param id of the {@link de.thbingen.epro.model.entity.CompanyObjective} to be returned
     * @return the {@link de.thbingen.epro.model.entity.CompanyObjective} with the given id
     */
    @GetMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('read')")
    public CompanyObjectiveDto findById(@PathVariable Long id) {
        Optional<CompanyObjectiveDto> result = companyObjectiveService.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new EntityNotFoundException("No CompanyObjective with this id exists");
    }

    /**
     * Adds a new {@link de.thbingen.epro.model.entity.CompanyObjective} with the given values
     * @param newCompanyObjective the new {@link de.thbingen.epro.model.entity.CompanyObjective} to be added
     * @return the newly added {@link de.thbingen.epro.model.entity.CompanyObjective}
     */
    @PostMapping(
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('change_CO_OKRs')")
    public ResponseEntity<CompanyObjectiveDto> addNew(@RequestBody @Valid CompanyObjectiveDto newCompanyObjective) {
        CompanyObjectiveDto companyObjectiveDto = companyObjectiveService.insertCompanyObjective(newCompanyObjective);
        return ResponseEntity.created(companyObjectiveDto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(companyObjectiveDto);
    }

    /**
     * Update the {@link de.thbingen.epro.model.entity.CompanyObjective} with the given id from the values in the request body
     * @param id of the {@link de.thbingen.epro.model.entity.CompanyObjective} to be updated
     * @param companyObjectiveDto the values to update the {@link de.thbingen.epro.model.entity.CompanyObjective} with
     * @return the newly updated {@link de.thbingen.epro.model.entity.CompanyObjective}
     */
    @PutMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('change_CO_OKRs')")
    public ResponseEntity<CompanyObjectiveDto> updateById(
            @PathVariable Long id,
            @RequestBody @Valid CompanyObjectiveDto companyObjectiveDto
    ) {
        if (!companyObjectiveService.existsById(id)) {
            throw new EntityNotFoundException("No CompanyObjective with this id exists");
        }

        return ResponseEntity.ok(companyObjectiveService.updateCompanyObjective(id, companyObjectiveDto));
    }

    /**
     * Delete the {@link de.thbingen.epro.model.entity.CompanyObjective} with the given id
     * @param id of the {@link de.thbingen.epro.model.entity.CompanyObjective} to be deleted
     * @return no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('change_CO_OKRs')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!companyObjectiveService.existsById(id)) {
            throw new EntityNotFoundException("No CompanyObjective with this id exists");
        }
        companyObjectiveService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all {@link de.thbingen.epro.model.entity.CompanyKeyResult}s belonging to the {@link de.thbingen.epro.model.entity.CompanyObjective}
     * with the given id
     *
     * @param id of the {@link de.thbingen.epro.model.entity.CompanyObjective} that the returned {@link de.thbingen.epro.model.entity.CompanyKeyResult}s should belong to
     * @param pageable the parameters determining which page to return
     * @return the requested page of {@link de.thbingen.epro.model.entity.CompanyKeyResult}s
     */
    @GetMapping(
            value = "/{id}/keyResults",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('read')")
    public PagedModel<EntityModel<CompanyKeyResultDto>> findAllCompanyKeyResultsByCompanyObjectiveId(
            @PageableDefault Pageable pageable,
            @PathVariable Long id
    ) {
        return companyKeyResultDtoPagedResourcesAssembler.toModel(companyKeyResultService.findAllByCompanyObjectiveId(id, pageable));
    }

    /**
     * Adds a new {@link de.thbingen.epro.model.entity.CompanyKeyResult} to the {@link de.thbingen.epro.model.entity.CompanyObjective}
     * with the given id
     *
     * @param id of the {@link de.thbingen.epro.model.entity.CompanyObjective} the {@link de.thbingen.epro.model.entity.CompanyKeyResult} should be added to
     * @param newCompanyKeyResultDto the {@link de.thbingen.epro.model.entity.CompanyKeyResult} to be added
     * @return the newly added {@link de.thbingen.epro.model.entity.CompanyKeyResult}
     */
    @PostMapping(
            value = "/{id}/keyResults",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('change_CO_OKRs')")
    public ResponseEntity<CompanyKeyResultDto> addNew(
            @PathVariable Long id,
            @RequestBody @Valid CompanyKeyResultPostDto newCompanyKeyResultDto
    ) {
        if (companyObjectiveService.existsById(id)) {
            CompanyKeyResultDto companyKeyResultDto = companyKeyResultService.insertCompanyKeyResultWithObjective(newCompanyKeyResultDto, id);
            return ResponseEntity.created(companyKeyResultDto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(companyKeyResultDto);
        }
        throw new EntityNotFoundException("No CompanyObjective with this id exists");
    }
}
