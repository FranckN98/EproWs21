package de.thbingen.epro.controller;

import de.thbingen.epro.exception.InvalidDateRangeException;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
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

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @PreAuthorize("hasAuthority('read')")
    public PagedModel<EntityModel<CompanyObjectiveDto>> findAll(
            @PageableDefault Pageable pageable,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> end
    ) {
        LocalDate startDate = start.orElse(LocalDate.now().with(firstDayOfYear()));
        LocalDate endDate = end.orElse(LocalDate.now().with(lastDayOfYear()));
        if(startDate.isAfter(endDate)) {
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

    @PostMapping(
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('change_CO_OKRs')")
    public ResponseEntity<CompanyObjectiveDto> addNew(@RequestBody @Valid CompanyObjectiveDto newCompanyObjective) {
        CompanyObjectiveDto companyObjectiveDto = companyObjectiveService.insertCompanyObjective(newCompanyObjective);
        return ResponseEntity.created(companyObjectiveDto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(companyObjectiveDto);
    }

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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('change_CO_OKRs')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!companyObjectiveService.existsById(id)) {
            throw new EntityNotFoundException("No CompanyObjective with this id exists");
        }
        companyObjectiveService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

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

    @PostMapping(
            value = "/{id}/keyResults",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('change_CO_OKRs')")
    public ResponseEntity<CompanyKeyResultDto> addNew(
            @PathVariable Long id,
            @RequestBody @Valid CompanyKeyResultDto newCompanyKeyResultDto
    ) {
        if (companyObjectiveService.existsById(id)) {
            CompanyKeyResultDto companyKeyResultDto = companyKeyResultService.insertCompanyKeyResultWithObjective(newCompanyKeyResultDto, id);
            return ResponseEntity.created(companyKeyResultDto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(companyKeyResultDto);
        }
        throw new EntityNotFoundException("No CompanyObjective with this id exists");
    }
}
