package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
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

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @PreAuthorize("hasAuthority('read')")
    public PagedModel<EntityModel<BusinessUnitKeyResultDto>> findAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(businessUnitKeyResultService.findAllBusinessUnitKeyResults(pageable));
    }

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

    @PutMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or hasAuthority('change_own_BU_OKRs')")
    public ResponseEntity<BusinessUnitKeyResultDto> updateById(
            @PathVariable Long id,
            @RequestBody @Valid BusinessUnitKeyResultDto businessUnitKeyResultDto
    ) {
        if (!businessUnitKeyResultService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnitKeyResult with this id exists");
        }
        return ResponseEntity.ok(businessUnitKeyResultService.updateBusinessUnitKeyResult(id, businessUnitKeyResultDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or hasAuthority('change_own_BU_OKRs')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!businessUnitKeyResultService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnitKeyResult with this id exists");
        }
        businessUnitKeyResultService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

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
