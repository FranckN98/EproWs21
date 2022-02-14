package de.thbingen.epro.controller;

import de.thbingen.epro.exception.NonMatchingIdsException;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.service.BusinessUnitKeyResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/businessUnitKeyResults")
public class BusinessUnitKeyResultController {

    private final BusinessUnitKeyResultService businessUnitKeyResultService;

    public BusinessUnitKeyResultController(BusinessUnitKeyResultService businessUnitKeyResultService) {
        this.businessUnitKeyResultService = businessUnitKeyResultService;
    }

    @GetMapping
    public Set<BusinessUnitKeyResultDto> findAll(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return businessUnitKeyResultService.getAllBusinessUnitKeyResults(pageNo, pageSize, sortBy);
    }

    @PostMapping
    public ResponseEntity<BusinessUnitKeyResultDto> addNew(@RequestBody @Valid BusinessUnitKeyResultDto newBusinessUnitKeyResultDto) {
        BusinessUnitKeyResultDto businessUnitKeyResultDto = businessUnitKeyResultService.saveBusinessUnitKeyResult(newBusinessUnitKeyResultDto);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/api/v1/businessUnitKeyResults/{id}")
                .buildAndExpand(businessUnitKeyResultDto.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(businessUnitKeyResultDto);
    }

    @GetMapping("/{id}")
    public BusinessUnitKeyResultDto findById(@PathVariable Long id) {
        Optional<BusinessUnitKeyResultDto> result = businessUnitKeyResultService.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new EntityNotFoundException("No BusinessUnitKeyResult with this id exists");
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessUnitKeyResultDto> updateById(@PathVariable Long id, @RequestBody BusinessUnitKeyResultDto businessUnitKeyResultDto) {
        if (businessUnitKeyResultDto.getId() == null) {
            businessUnitKeyResultDto.setId(id);
        }
        if (!Objects.equals(businessUnitKeyResultDto.getId(), id)) {
            throw new NonMatchingIdsException("Ids in path and jsonObject do not match");
        }
        if (!businessUnitKeyResultService.existsById(id)) {
            return this.addNew(businessUnitKeyResultDto);
        }
        return ResponseEntity.ok(businessUnitKeyResultService.saveBusinessUnitKeyResult(businessUnitKeyResultDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!businessUnitKeyResultService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnitKeyResult with this id exists");
        }
        businessUnitKeyResultService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
