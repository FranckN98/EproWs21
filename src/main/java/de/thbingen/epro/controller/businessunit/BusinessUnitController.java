package de.thbingen.epro.controller.businessunit;

import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.service.BusinessUnitService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/businessUnits")
public class BusinessUnitController {

    private final BusinessUnitService businessUnitService;
    private final PagedResourcesAssembler<BusinessUnitDto> pagedResourcesAssembler;

    public BusinessUnitController(BusinessUnitService businessUnitService, PagedResourcesAssembler<BusinessUnitDto> pagedResourcesAssembler, PagedResourcesAssembler<BusinessUnitObjectiveDto> test) {
        this.businessUnitService = businessUnitService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public PagedModel<EntityModel<BusinessUnitDto>> findAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(businessUnitService.findAll(pageable));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public BusinessUnitDto findById(@PathVariable Long id) {
        Optional<BusinessUnitDto> result = businessUnitService.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new EntityNotFoundException("No BusinessUnit with this id exists");
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessUnitDto> addNew(@RequestBody @Valid BusinessUnitDto newBusinessUnit) {
        BusinessUnitDto businessUnitDto = businessUnitService.saveBusinessUnit(newBusinessUnit);
        return ResponseEntity
                .created(businessUnitDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(businessUnitDto);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessUnitDto> updateById(@PathVariable Long id, @RequestBody @Valid BusinessUnitDto businessUnitDto) {
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
}
