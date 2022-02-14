package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.CompanyKeyResultHistoryDto;
import de.thbingen.epro.service.CompanyKeyResultHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/companyKeyResultHistory")
public class CompanyKeyResultHistoryController {

    private final CompanyKeyResultHistoryService companyKeyResultHistoryService;

    public CompanyKeyResultHistoryController(CompanyKeyResultHistoryService companyKeyResultHistoryService) {
        this.companyKeyResultHistoryService = companyKeyResultHistoryService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public List<CompanyKeyResultHistoryDto> getAll() {
        return companyKeyResultHistoryService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public CompanyKeyResultHistoryDto getById(@PathVariable Long id) {
        Optional<CompanyKeyResultHistoryDto> result = companyKeyResultHistoryService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No CompanyKeyResultHistory with this id exists");
    }

    // region Forbidden Methods

    /**
     * Historization is handled using DB-triggers, so Posting CompanyKeyResultHistories is not allowed.
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
     * History should not be deleted, delete history by deleting the CompanyKeyResult.
     * This method has only been added for documentation purposes.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public void delete() {
    }

    // endregion
}
