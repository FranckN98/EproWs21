package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.service.BusinessUnitKeyResultHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/businessUnitKeyResultHistory")
public class BusinessUnitKeyResultHistoryController {

    private final BusinessUnitKeyResultHistoryService businessUnitKeyResultHistoryService;

    public BusinessUnitKeyResultHistoryController(BusinessUnitKeyResultHistoryService businessUnitKeyResultHistoryService) {
        this.businessUnitKeyResultHistoryService = businessUnitKeyResultHistoryService;
    }

    @GetMapping
    public List<BusinessUnitKeyResultHistoryDto> getAll() {
        return businessUnitKeyResultHistoryService.findAll();
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
