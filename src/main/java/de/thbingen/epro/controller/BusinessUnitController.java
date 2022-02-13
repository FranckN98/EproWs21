package de.thbingen.epro.controller;

import de.thbingen.epro.exception.NonMatchingIdsException;
import de.thbingen.epro.model.business.BusinessUnit;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.service.BusinessUnitObjectiveService;
import de.thbingen.epro.service.BusinessUnitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/businessUnits")
public class BusinessUnitController {

    final BusinessUnitService businessUnitService;
    private final BusinessUnitObjectiveService businessUnitObjectiveService;

    public BusinessUnitController(BusinessUnitService businessUnitService,BusinessUnitObjectiveService businessUnitObjectiveService) {
        this.businessUnitService = businessUnitService;
        this.businessUnitObjectiveService = businessUnitObjectiveService;
    }

    @GetMapping
    public List<BusinessUnitDto> findAll() {
        return businessUnitService.findAll();
    }

    @GetMapping("/{id}")
    public BusinessUnitDto findById(@PathVariable Long id) {
        Optional<BusinessUnitDto> result = businessUnitService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No BusinessUnit with this id exists");
    }

    @PostMapping
    public ResponseEntity<BusinessUnitDto> addNew(@RequestBody @Valid BusinessUnitDto newBusinessUnit) {
        BusinessUnitDto businessUnitDto = businessUnitService.saveBusinessUnit(newBusinessUnit);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/api/v1/businessUnits/{id}")
                .buildAndExpand(businessUnitDto.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(businessUnitDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessUnitDto> updateById(@PathVariable Long id, @RequestBody @Valid BusinessUnitDto businessUnitDto) {
        if (businessUnitDto.getId() == null) {
            businessUnitDto.setId(id);
        }
        if (!Objects.equals(businessUnitDto.getId(), id)) {
            throw new NonMatchingIdsException("Ids in path and jsonObject do not match");
        }

        if (!businessUnitService.existsById(id)) {
            return this.addNew(businessUnitDto);
        }

        return ResponseEntity.ok(businessUnitService.saveBusinessUnit(businessUnitDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!businessUnitService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnit with this id exists");
        }
        businessUnitService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{id}/objectives")
    public ResponseEntity<BusinessUnitObjectiveDto> addNewBusinessUnitObjective(@PathVariable Long id, @RequestBody @Valid BusinessUnitObjectiveDto newBusinessUnitObjectiveDto) {
        BusinessUnitDto businessUnit = businessUnitService.findById(id).get();
        BusinessUnitObjectiveDto businessUnitObjectiveDto = businessUnitObjectiveService.saveBusinessUnitObjectiveWithBusinessUnit(newBusinessUnitObjectiveDto,businessUnit);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/api/v1/businessUnitObjectives/{id}")
                .buildAndExpand(businessUnitObjectiveDto.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(businessUnitObjectiveDto);
    }
}
