package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.CompanyKeyResultHistoryDto;
import de.thbingen.epro.service.CompanyKeyResultHistoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * This Controller is responsible for everything under the /companyKeyResultHistory endpoint
 */
@RestController
@RequestMapping("/companyKeyResultHistory")
public class CompanyKeyResultHistoryController {

    private final CompanyKeyResultHistoryService companyKeyResultHistoryService;
    private final PagedResourcesAssembler<CompanyKeyResultHistoryDto> pagedResourcesAssembler;

    public CompanyKeyResultHistoryController(CompanyKeyResultHistoryService companyKeyResultHistoryService, PagedResourcesAssembler<CompanyKeyResultHistoryDto> pagedResourcesAssembler) {
        this.companyKeyResultHistoryService = companyKeyResultHistoryService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    /**
     * Returns all {@link de.thbingen.epro.model.entity.CompanyKeyResultHistory} items
     * @param pageable the parameters determining which page to return
     * @return the requested page of {@link de.thbingen.epro.model.entity.CompanyKeyResultHistory} items
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @PreAuthorize("hasAuthority('read')")
    public PagedModel<EntityModel<CompanyKeyResultHistoryDto>> getAll(
            @PageableDefault Pageable pageable
    ) {
        return pagedResourcesAssembler.toModel(companyKeyResultHistoryService.findAll(pageable));
    }

    /**
     * return the {@link de.thbingen.epro.model.entity.CompanyKeyResultHistory} item with the given id
     * @param id of the {@link de.thbingen.epro.model.entity.CompanyKeyResultHistory} to be returned
     * @return the requested {@link de.thbingen.epro.model.entity.CompanyKeyResultHistory} item
     */
    @GetMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('read')")
    public CompanyKeyResultHistoryDto getById(@PathVariable Long id) {
        Optional<CompanyKeyResultHistoryDto> result = companyKeyResultHistoryService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No CompanyKeyResultHistory with this id exists");
    }
}
