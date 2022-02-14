package de.thbingen.epro.controller;

import de.thbingen.epro.exception.NonMatchingIdsException;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.service.BusinessUnitService;
import org.springframework.beans.factory.annotation.Value;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/businessUnits")
public class BusinessUnitController {

    private final BusinessUnitService businessUnitService;
    private final PagedResourcesAssembler<BusinessUnitDto> pagedResourcesAssembler;


    @Value("${server.port}")
    private int serverPort;

    public BusinessUnitController(BusinessUnitService businessUnitService, PagedResourcesAssembler<BusinessUnitDto> pagedResourcesAssembler) {
        this.businessUnitService = businessUnitService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<BusinessUnitDto>> findAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return pagedResourcesAssembler.toModel(businessUnitService.findAll(page, size, sortBy));
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
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(serverPort)
                .path("/api/v1/businessUnits/{id}")
                .buildAndExpand(businessUnitDto.getId());
        return ResponseEntity.created(linkTo(methodOn(BusinessUnitController.class).findById(businessUnitDto.getId())).withSelfRel().toUri()).body(businessUnitDto);
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

    @PostMapping("/{id}/objectives")
    public ResponseEntity<BusinessUnitObjectiveDto> addNew(
            @PathVariable Long id,
            @RequestBody @Valid BusinessUnitObjectiveDto newBusinessUnitObj
    ) {
        BusinessUnitDto businessUnit = businessUnitService.findById(id).get();
        BusinessUnitObjectiveDto businessUnitDto = businessUnitService.saveBusinessUnitObjective(newBusinessUnitObj, businessUnit);
        return ResponseEntity.created(linkTo(methodOn(BusinessUnitController.class).findById(businessUnitDto.getId())).withSelfRel().toUri()).body(businessUnitDto);
    }
}
