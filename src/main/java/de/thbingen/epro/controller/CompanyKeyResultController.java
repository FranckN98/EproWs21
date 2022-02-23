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
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
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
    public PagedModel<EntityModel<CompanyKeyResultDto>> findAll(
            @PageableDefault Pageable pageable
    ) {
        Page<CompanyKeyResultDto> allCompanyKeyResults = companyKeyResultService.getAllCompanyKeyResults(pageable);
        return pagedResourcesAssembler.toModel(allCompanyKeyResults);
    }

    @PostMapping
    public ResponseEntity<CompanyKeyResultDto> addNew(@RequestBody @Valid CompanyKeyResultDto newCompanyKeyResultDto) {
        CompanyKeyResultDto companyKeyResultDto = companyKeyResultService.insertCompanyKeyResult(newCompanyKeyResultDto);
        return ResponseEntity.created(companyKeyResultDto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(companyKeyResultDto);
    }

    @GetMapping("/{id}")
    public CompanyKeyResultDto findById(@PathVariable Long id) {
        Optional<CompanyKeyResultDto> result = companyKeyResultService.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new EntityNotFoundException("No CompanyKeyResult with this id exists");
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyKeyResultDto> updateById(
            @PathVariable Long id,
            @RequestBody @Valid CompanyKeyResultDto companyKeyResultDto
    ) {
        if (!companyKeyResultService.existsById(id)) {
            return this.addNew(companyKeyResultDto);
        }
        return ResponseEntity.ok(companyKeyResultService.updateCompanyKeyResult(id, companyKeyResultDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!companyKeyResultService.existsById(id)) {
            throw new EntityNotFoundException("No CompanyKeyResult with this id exists");
        }
        companyKeyResultService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/history", produces = MediaTypes.HAL_JSON_VALUE)
    public PagedModel<EntityModel<CompanyKeyResultHistoryDto>> getHistory(
            @PageableDefault Pageable pageable,
            @PathVariable Long id
    ) {
        return companyKeyResultHistoryDtoPagedResourcesAssembler.toModel(
                companyKeyResultHistoryService.getAllByCompanyKeyResultId(id, pageable)
        );
    }
}
