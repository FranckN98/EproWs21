package de.thbingen.epro.controller;

import de.thbingen.epro.exception.NonMatchingIdsException;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.service.BusinessUnitKeyResultService;
import de.thbingen.epro.service.BusinessUnitObjectiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/businessUnitObjectives")
public class BusinessUnitObjectiveController {

    private final BusinessUnitObjectiveService businessUnitObjectiveService;
    private final BusinessUnitKeyResultService businessUnitKeyResultService;

    public BusinessUnitObjectiveController(BusinessUnitKeyResultService businessUnitKeyResultService,BusinessUnitObjectiveService businessUnitObjectiveService) {
        this.businessUnitObjectiveService = businessUnitObjectiveService;
        this.businessUnitKeyResultService = businessUnitKeyResultService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public Set<BusinessUnitObjectiveDto> findAll(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return businessUnitObjectiveService.getAllBusinessUnitObjectives(pageNo, pageSize, sortBy);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or hasAuthority('change_own_BU_OKRs')")
    public ResponseEntity<BusinessUnitObjectiveDto> addNew(@RequestBody @Valid BusinessUnitObjectiveDto newBusinessUnitObjectiveDto) {
        BusinessUnitObjectiveDto businessUnitObjectiveDto = businessUnitObjectiveService.saveBusinessUnitObjective(newBusinessUnitObjectiveDto);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/api/v1/businessUnitObjectives/{id}")
                .buildAndExpand(businessUnitObjectiveDto.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(businessUnitObjectiveDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public BusinessUnitObjectiveDto findById(@PathVariable Long id) {
        Optional<BusinessUnitObjectiveDto> result = businessUnitObjectiveService.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new EntityNotFoundException("No BusinessUnitObjective with this id exists");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or hasAuthority('change_own_BU_OKRs')")
    public ResponseEntity<BusinessUnitObjectiveDto> updateById(@PathVariable Long id, @RequestBody BusinessUnitObjectiveDto businessUnitObjectiveDto) {
        if (businessUnitObjectiveDto.getId() == null) {
            businessUnitObjectiveDto.setId(id);
        }
        if (!Objects.equals(businessUnitObjectiveDto.getId(), id)) {
            throw new NonMatchingIdsException("Ids in path and jsonObject do not match");
        }

        if (!businessUnitObjectiveService.existsById(id)) {
            return this.addNew(businessUnitObjectiveDto);
        }

        return ResponseEntity.ok(businessUnitObjectiveService.saveBusinessUnitObjective(businessUnitObjectiveDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or hasAuthority('change_own_BU_OKRs')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!businessUnitObjectiveService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnitObjective with this id exists");
        }
        businessUnitObjectiveService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/keyresults")
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or hasAuthority('change_own_BU_OKRs')")
    public ResponseEntity<BusinessUnitKeyResultDto> addNewKeyResult(@PathVariable Long id,
                                                                    @RequestBody @Valid BusinessUnitKeyResultDto newBusinessUnitKeyResultDto) {
        BusinessUnitObjectiveDto businessUnitObjectiveDto = businessUnitObjectiveService.findById(id).get();
        BusinessUnitKeyResultDto businessUnitKeyResultDto = businessUnitKeyResultService.saveBusinessUnitKeyResultWithObjective(newBusinessUnitKeyResultDto, businessUnitObjectiveDto);

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/api/v1/businessUnitKeyResults/{id}")
                .buildAndExpand(businessUnitKeyResultDto.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(businessUnitKeyResultDto);
    }
}
