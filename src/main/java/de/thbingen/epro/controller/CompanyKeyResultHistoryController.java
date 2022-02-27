package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.CompanyKeyResultHistoryDto;
import de.thbingen.epro.service.CompanyKeyResultHistoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
@RequestMapping("/companyKeyResultHistory")
public class CompanyKeyResultHistoryController {

    private final CompanyKeyResultHistoryService companyKeyResultHistoryService;
    private final PagedResourcesAssembler<CompanyKeyResultHistoryDto> pagedResourcesAssembler;

    public CompanyKeyResultHistoryController(CompanyKeyResultHistoryService companyKeyResultHistoryService, PagedResourcesAssembler<CompanyKeyResultHistoryDto> pagedResourcesAssembler) {
        this.companyKeyResultHistoryService = companyKeyResultHistoryService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public PagedModel<EntityModel<CompanyKeyResultHistoryDto>> getAll(
            @PageableDefault Pageable pageable
    ) {
        return pagedResourcesAssembler.toModel(companyKeyResultHistoryService.findAll(pageable));
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    public CompanyKeyResultHistoryDto getById(@PathVariable Long id) {
        Optional<CompanyKeyResultHistoryDto> result = companyKeyResultHistoryService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No CompanyKeyResultHistory with this id exists");
    }
}
