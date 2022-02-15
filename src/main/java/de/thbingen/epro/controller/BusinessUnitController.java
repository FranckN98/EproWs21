package de.thbingen.epro.controller;

import de.thbingen.epro.exception.NonMatchingIdsException;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.service.BusinessUnitObjectiveService;
import de.thbingen.epro.service.BusinessUnitService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/businessUnits")
public class BusinessUnitController {

    private final BusinessUnitService businessUnitService;
    private final BusinessUnitObjectiveService businessUnitObjectiveService;
    private final PagedResourcesAssembler<BusinessUnitDto> pagedResourcesAssembler;

    public BusinessUnitController(BusinessUnitService businessUnitService, BusinessUnitObjectiveService businessUnitObjectiveService, PagedResourcesAssembler<BusinessUnitDto> pagedResourcesAssembler) {
        this.businessUnitService = businessUnitService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.businessUnitObjectiveService = businessUnitObjectiveService;
    }

    @GetMapping
    public CollectionModel<EntityModel<BusinessUnitDto>> findAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(businessUnitService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public BusinessUnitDto findById(@PathVariable Long id) {
        Optional<BusinessUnitDto> result = businessUnitService.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new EntityNotFoundException("No BusinessUnit with this id exists");
    }

    @PostMapping
    public ResponseEntity<BusinessUnitDto> addNew(@RequestBody @Valid BusinessUnitDto newBusinessUnit) {
        BusinessUnitDto businessUnitDto = businessUnitService.saveBusinessUnit(newBusinessUnit);
        return ResponseEntity.created(linkTo(methodOn(BusinessUnitController.class).findById(businessUnitDto.getId())).withSelfRel().toUri()).body(businessUnitDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessUnitDto> updateById(@PathVariable Long id, @RequestBody @Valid BusinessUnitDto businessUnitDto) {
        if (!businessUnitService.existsById(id)) {
            return this.addNew(businessUnitDto);
            // TODO: Wenn wir kein upsert wollen:
            // throw new EntityNotFoundException("No BusinessUnit with this id exists");
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

    @GetMapping("/{id}/objectives")
    public ResponseEntity<Set<BusinessUnitObjectiveDto>> getAllBusinessUnitObjectives(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Set<BusinessUnitObjectiveDto> result = businessUnitObjectiveService.getAllByBusinessUnitId(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/objectives")
    public ResponseEntity<BusinessUnitObjectiveDto> addNewBusinessUnitObjective(
            @PathVariable Long id,
            @RequestBody @Valid BusinessUnitObjectiveDto newBusinessUnitObjectiveDto
    ) {
        BusinessUnitDto businessUnit = businessUnitService.findById(id).get();
        BusinessUnitObjectiveDto businessUnitObjectiveDto = businessUnitObjectiveService.saveBusinessUnitObjectiveWithBusinessUnit(newBusinessUnitObjectiveDto, businessUnit);
        UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(8080).path("/api/v1/businessUnitObjectives/{id}").buildAndExpand(businessUnitObjectiveDto.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(businessUnitObjectiveDto);
    }
}
