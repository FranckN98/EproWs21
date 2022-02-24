package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.service.BusinessUnitObjectiveService;
import de.thbingen.epro.service.BusinessUnitService;
import de.thbingen.epro.service.OkrUserService;
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
    private final BusinessUnitObjectiveService businessUnitObjectiveService;
    private final PagedResourcesAssembler<BusinessUnitObjectiveDto> pagedResourcesAssemblerObjective;

    private final OkrUserService okrUserService;
    private final PagedResourcesAssembler<OkrUserDto> pagedResourcesAssemblerOkrUser;

    public BusinessUnitController(BusinessUnitService businessUnitService, PagedResourcesAssembler<BusinessUnitDto> pagedResourcesAssembler, BusinessUnitObjectiveService businessUnitObjectiveService, PagedResourcesAssembler<BusinessUnitObjectiveDto> pagedResourcesAssemblerObjective, OkrUserService okrUserService, PagedResourcesAssembler<OkrUserDto> pagedResourcesAssemblerOkrUser) {
        this.businessUnitService = businessUnitService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.businessUnitObjectiveService = businessUnitObjectiveService;
        this.pagedResourcesAssemblerObjective = pagedResourcesAssemblerObjective;
        this.okrUserService = okrUserService;
        this.pagedResourcesAssemblerOkrUser = pagedResourcesAssemblerOkrUser;
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
        BusinessUnitDto businessUnitDto = businessUnitService.insertBusinessUnit(newBusinessUnit);
        return ResponseEntity
                .created(businessUnitDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(businessUnitDto);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessUnitDto> updateById(@PathVariable Long id, @RequestBody @Valid BusinessUnitDto businessUnitDto) {
        if (!businessUnitService.existsById(id)) {
            return this.addNew(businessUnitDto);
        }

        return ResponseEntity.ok(businessUnitService.updateBusinessUnit(id, businessUnitDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!businessUnitService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnit with this id exists");
        }
        businessUnitService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // region BusinessUnitObjective

    @GetMapping(
            value = "/{id}/objectives",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    public PagedModel<EntityModel<BusinessUnitObjectiveDto>> getAllBusinessUnitObjectives(
            @PageableDefault Pageable pageable,
            @PathVariable Long id
    ) {
        return pagedResourcesAssemblerObjective.toModel(businessUnitObjectiveService.getAllByBusinessUnitId(id, pageable));
    }

    @PostMapping(
            value = "/{id}/objectives",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BusinessUnitObjectiveDto> addNewBusinessUnitObjective(
            @PathVariable Long id,
            @RequestBody @Valid BusinessUnitObjectiveDto newBusinessUnitObjectiveDto
    ) {
        if (businessUnitService.existsById(id)) {
            BusinessUnitObjectiveDto businessUnitObjectiveDto = businessUnitObjectiveService.insertBusinessUnitObjectiveWithBusinessUnit(newBusinessUnitObjectiveDto, id);
            return ResponseEntity.created(businessUnitObjectiveDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(businessUnitObjectiveDto);
        }

        throw new EntityNotFoundException("No BusinessUnit with the given ID exists");
    }

    // endregion

    // region okruser

    @GetMapping(
            value = "/{id}/users",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    public PagedModel<EntityModel<OkrUserDto>> getAllOkrUsers(@PageableDefault Pageable pageable, @PathVariable Long id) {
        return pagedResourcesAssemblerOkrUser.toModel(okrUserService.findAllByBusinessUnitId(id, pageable));
    }

    @PostMapping(
            value = "/{id}/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaTypes.HAL_JSON_VALUE
    )
    public OkrUserDto addNewUser() {
        //TODO
        return new OkrUserDto();
    }

    // endregion
}
