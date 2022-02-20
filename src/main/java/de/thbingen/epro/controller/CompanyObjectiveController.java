package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.service.CompanyKeyResultService;
import de.thbingen.epro.service.CompanyObjectiveService;
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
@RequestMapping("/companyobjectives")
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

    @GetMapping
    public PagedModel<EntityModel<CompanyObjectiveDto>> findAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(companyObjectiveService.getAllCompanyObjectives(pageable));
    }

    @PostMapping
    public ResponseEntity<CompanyObjectiveDto> addNew(@RequestBody @Valid CompanyObjectiveDto newCompanyObjective) {
        CompanyObjectiveDto companyObjectiveDto = companyObjectiveService.saveCompanyObjective(newCompanyObjective);
        return ResponseEntity.created(companyObjectiveDto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(companyObjectiveDto);
    }

    @GetMapping("/{id}")
    public CompanyObjectiveDto findById(@PathVariable Long id) {
        Optional<CompanyObjectiveDto> result = companyObjectiveService.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new EntityNotFoundException("No CompanyObjective with this id exists");
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyObjectiveDto> updateById(
            @PathVariable Long id,
            @RequestBody @Valid CompanyObjectiveDto companyObjectiveDto
    ) {
        if (!companyObjectiveService.existsById(id)) {
            return this.addNew(companyObjectiveDto);
        }

        return ResponseEntity.ok(companyObjectiveService.saveCompanyObjective(companyObjectiveDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!companyObjectiveService.existsById(id)) {
            throw new EntityNotFoundException("No CompanyObjective with this id exists");
        }
        companyObjectiveService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/keyresults", produces = MediaTypes.HAL_JSON_VALUE)
    public PagedModel<EntityModel<CompanyKeyResultDto>> getAllCompanyKeyResults(
            @PageableDefault Pageable pageable,
            @PathVariable Long id
    ) {
        return companyKeyResultDtoPagedResourcesAssembler.toModel(companyKeyResultService.getAllByCompanyObjectiveId(id, pageable));
    }

    @PostMapping("/{id}/keyresults")
    public ResponseEntity<CompanyKeyResultDto> addNew(
            @PathVariable Long id,
            @RequestBody @Valid CompanyKeyResultDto newCompanyKeyResultDto
    ) {
        Optional<CompanyObjectiveDto> companyObjectiveDto = companyObjectiveService.findById(id);
        if(companyObjectiveDto.isPresent()) {
            CompanyKeyResultDto companyKeyResultDto = companyKeyResultService.saveCompanyKeyResultWithObjective(newCompanyKeyResultDto, companyObjectiveDto.get());
            return ResponseEntity.created(companyKeyResultDto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(companyKeyResultDto);
        }
        throw new EntityNotFoundException("No CompanyObjective with this id exists");
    }
}
