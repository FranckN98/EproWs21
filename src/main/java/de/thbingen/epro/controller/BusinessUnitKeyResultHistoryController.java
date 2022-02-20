package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.service.BusinessUnitKeyResultHistoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
@RequestMapping("/businessUnitKeyResultHistory")
public class BusinessUnitKeyResultHistoryController {

    private final BusinessUnitKeyResultHistoryService businessUnitKeyResultHistoryService;
    private final PagedResourcesAssembler<BusinessUnitKeyResultHistoryDto> pagedResourcesAssembler;

    public BusinessUnitKeyResultHistoryController(BusinessUnitKeyResultHistoryService businessUnitKeyResultHistoryService, PagedResourcesAssembler<BusinessUnitKeyResultHistoryDto> pagedResourcesAssembler) {
        this.businessUnitKeyResultHistoryService = businessUnitKeyResultHistoryService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public PagedModel<EntityModel<BusinessUnitKeyResultHistoryDto>> getAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(businessUnitKeyResultHistoryService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public BusinessUnitKeyResultHistoryDto getById(@PathVariable Long id) {
        Optional<BusinessUnitKeyResultHistoryDto> result = businessUnitKeyResultHistoryService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No BusinessUnitKeyResultHistory with this id exists");
    }

    // region Forbidden Methods

    /**
     * Historization is handled using DB-triggers, so Posting BusinessUnitKeyResultHistories is not allowed.
     * This method has only been added for documentation purposes.
     */
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public void addNew() {
    }

    /**
     * History should not be updated.
     * This method has only been added for documentation purposes.
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public void update() {
    }

    /**
     * History should not be deleted, delete history by deleting the BusinessUnitKeyResult.
     * This method has only been added for documentation purposes.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public void delete() {
    }

    // endregion
}
