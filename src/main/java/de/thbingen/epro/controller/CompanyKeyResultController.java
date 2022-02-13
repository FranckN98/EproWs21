package de.thbingen.epro.controller;

import de.thbingen.epro.exception.NonMatchingIdsException;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.service.CompanyKeyResultService;
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
@RequestMapping("/companyKeyResults")
public class CompanyKeyResultController {

    private final CompanyKeyResultService companyKeyResultService;

    public CompanyKeyResultController(CompanyKeyResultService companyKeyResultService) {
        this.companyKeyResultService = companyKeyResultService;
    }
    @GetMapping
    public Set<CompanyKeyResultDto> findAll(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return companyKeyResultService.getAllCompanyKeyResults(pageNo, pageSize, sortBy);
    }
    @PostMapping
    public ResponseEntity<CompanyKeyResultDto> addNew(@RequestBody @Valid CompanyKeyResultDto newCompanyKeyResultDto) {
        CompanyKeyResultDto companyKeyResultDto = companyKeyResultService.saveCompanyKeyResult(newCompanyKeyResultDto);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/api/v1/companyKeyResults/{id}")
                .buildAndExpand(companyKeyResultDto.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(companyKeyResultDto);
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
    public ResponseEntity<CompanyKeyResultDto> updateById(@PathVariable Long id, @RequestBody CompanyKeyResultDto companyKeyResultDto) {
        if (companyKeyResultDto.getId() == null) {
            companyKeyResultDto.setId(id);
        }
        if (!Objects.equals(companyKeyResultDto.getId(), id)) {
            throw new NonMatchingIdsException("Ids in path and jsonObject do not match");
        }
        if (!companyKeyResultService.existsById(id)) {
            return this.addNew(companyKeyResultDto);
        }
        return ResponseEntity.ok(companyKeyResultService.saveCompanyKeyResult(companyKeyResultDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!companyKeyResultService.existsById(id)) {
            throw new EntityNotFoundException("No CompanyKeyResult with this id exists");
        }
        companyKeyResultService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
