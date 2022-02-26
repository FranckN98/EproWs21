package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyKeyResultHistoryDto;
import de.thbingen.epro.service.CompanyKeyResultHistoryService;
import de.thbingen.epro.service.CompanyKeyResultService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

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

    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public PagedModel<EntityModel<CompanyKeyResultDto>> findAll(
            @PageableDefault Pageable pageable
    ) {
        Page<CompanyKeyResultDto> allCompanyKeyResults = companyKeyResultService.findAllCompanyKeyResults(pageable);
        return pagedResourcesAssembler.toModel(allCompanyKeyResults);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public CompanyKeyResultDto findById(@PathVariable Long id) {
        Optional<CompanyKeyResultDto> result = companyKeyResultService.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new EntityNotFoundException("No CompanyKeyResult with this id exists");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('change_CO_OKRs')")
    public ResponseEntity<CompanyKeyResultDto> updateById(
            @PathVariable Long id,
            @RequestBody @Valid CompanyKeyResultDto companyKeyResultDto
    ) {
        if (!companyKeyResultService.existsById(id)) {
            throw new EntityNotFoundException("No CompanyKeyResult with this id exists");
        }
        return ResponseEntity.ok(companyKeyResultService.updateCompanyKeyResult(id, companyKeyResultDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('change_CO_OKRs')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!companyKeyResultService.existsById(id)) {
            throw new EntityNotFoundException("No CompanyKeyResult with this id exists");
        }
        companyKeyResultService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

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
