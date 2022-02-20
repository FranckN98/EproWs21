package de.thbingen.epro.controller.businessunitobjective;

import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.service.BusinessUnitObjectiveService;
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
@RequestMapping("/businessUnitObjectives")
public class BusinessUnitObjectiveController {

    private final BusinessUnitObjectiveService businessUnitObjectiveService;
    private final PagedResourcesAssembler<BusinessUnitObjectiveDto> pagedResourcesAssembler;

    public BusinessUnitObjectiveController(BusinessUnitObjectiveService businessUnitObjectiveService, PagedResourcesAssembler<BusinessUnitObjectiveDto> pagedResourcesAssembler) {
        this.businessUnitObjectiveService = businessUnitObjectiveService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public PagedModel<EntityModel<BusinessUnitObjectiveDto>> findAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(businessUnitObjectiveService.getAllBusinessUnitObjectives(pageable));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public BusinessUnitObjectiveDto findById(@PathVariable Long id) {
        Optional<BusinessUnitObjectiveDto> result = businessUnitObjectiveService.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new EntityNotFoundException("No BusinessUnitObjective with this id exists");
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<BusinessUnitObjectiveDto> addNew(@RequestBody @Valid BusinessUnitObjectiveDto newBusinessUnitObjectiveDto) {
        BusinessUnitObjectiveDto businessUnitObjectiveDto = businessUnitObjectiveService.saveBusinessUnitObjective(newBusinessUnitObjectiveDto);
        return ResponseEntity.created(businessUnitObjectiveDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(businessUnitObjectiveDto);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<BusinessUnitObjectiveDto> updateById(
            @PathVariable Long id,
            @RequestBody @Valid BusinessUnitObjectiveDto businessUnitObjectiveDto
    ) {
        if (!businessUnitObjectiveService.existsById(id)) {
            return this.addNew(businessUnitObjectiveDto);
        }

        return ResponseEntity.ok(businessUnitObjectiveService.saveBusinessUnitObjective(businessUnitObjectiveDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!businessUnitObjectiveService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnitObjective with this id exists");
        }
        businessUnitObjectiveService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
