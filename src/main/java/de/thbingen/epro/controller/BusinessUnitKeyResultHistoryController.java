package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.service.BusinessUnitKeyResultHistoryService;
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

/**
 * This controller is responsible for the /businessUnitKeyResultHistory endpoint
 */
@RestController
@RequestMapping("/businessUnitKeyResultHistory")
public class BusinessUnitKeyResultHistoryController {

    private final BusinessUnitKeyResultHistoryService businessUnitKeyResultHistoryService;
    private final PagedResourcesAssembler<BusinessUnitKeyResultHistoryDto> pagedResourcesAssembler;

    public BusinessUnitKeyResultHistoryController(BusinessUnitKeyResultHistoryService businessUnitKeyResultHistoryService, PagedResourcesAssembler<BusinessUnitKeyResultHistoryDto> pagedResourcesAssembler) {
        this.businessUnitKeyResultHistoryService = businessUnitKeyResultHistoryService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    /**
     * Returns all {@link de.thbingen.epro.model.entity.BusinessUnitKeyResultHistory} Items
     * @param pageable The parameters determining which page to return
     * @return the requested page of historical BusinessUnitKeyResults
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public PagedModel<EntityModel<BusinessUnitKeyResultHistoryDto>> getAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(businessUnitKeyResultHistoryService.findAll(pageable));
    }

    /**
     * Returns the {@link de.thbingen.epro.model.entity.BusinessUnitKeyResultHistory} Item with the given id
     * @param id of the BusinessUnitKeyResultHistory Item to be returned
     * @return the requested historical BusinessUnitKeyResult
     */
    @GetMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    public BusinessUnitKeyResultHistoryDto getById(@PathVariable Long id) {
        Optional<BusinessUnitKeyResultHistoryDto> result = businessUnitKeyResultHistoryService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No BusinessUnitKeyResultHistory with this id exists");
    }
}
