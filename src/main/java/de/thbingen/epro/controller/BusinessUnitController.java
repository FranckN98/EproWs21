package de.thbingen.epro.controller;

import de.thbingen.epro.exception.InvalidDateRangeException;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.dto.OkrUserPostDto;
import de.thbingen.epro.service.BusinessUnitObjectiveService;
import de.thbingen.epro.service.BusinessUnitService;
import de.thbingen.epro.service.OkrUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

/**
 * This Controller is responsible for returning all resources under the /businessUnits path
 */
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

    /**
     * Returns all Business Units of the requested Page
     *
     * @param pageable Allows requesting a certain page of a certain size with a certain sort
     * @return The requested Page of Business Units
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @PreAuthorize("hasAuthority('read')")
    public PagedModel<EntityModel<BusinessUnitDto>> findAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(businessUnitService.findAll(pageable));
    }

    /**
     * Returns the BusinessUnit with the given id.
     * <p>
     * Will throw an EntityNotFoundException if there is no BusinessUnit with the given id
     *
     * @param id the id of the requested BusinessUnit
     * @return the requested BusinessUnit
     */
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @PreAuthorize("hasAuthority('read')")
    public BusinessUnitDto findById(@PathVariable Long id) {
        Optional<BusinessUnitDto> result = businessUnitService.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new EntityNotFoundException("No BusinessUnit with this id exists");
    }

    /**
     * Adds the BusinessUnit given in the RequestBody.
     * The location header will contain the location at which the new BusinessUnit can be queried
     *
     * @param newBusinessUnit The BusinessUnit to be added
     * @return the newly added BusinessUnit
     */
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('change_all_BU_OKRs')")
    public ResponseEntity<BusinessUnitDto> addNew(@RequestBody @Valid BusinessUnitDto newBusinessUnit) {
        BusinessUnitDto businessUnitDto = businessUnitService.insertBusinessUnit(newBusinessUnit);
        return ResponseEntity
                .created(businessUnitDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(businessUnitDto);
    }

    /**
     * Update the BusinessUnit with the given id with the BusinessUnit in the Request Body.
     *
     * Will throw an EntityNotFoundException if there is no BusinessUnit with the given id
     *
     * @param id    the id of the BusinessUnit to be updated
     * @param businessUnitDto the new values for the BusinessUnit
     * @return the newly updated BusinessUnit
     */
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or (hasAuthority('change_own_BU_OKRs') and @customExpressions.belongsToBusinessUnit(#id, principal.username))")
    public ResponseEntity<BusinessUnitDto> updateById(@PathVariable Long id, @RequestBody @Valid BusinessUnitDto businessUnitDto) {
        if (!businessUnitService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnit with this id exists");
        }

        return ResponseEntity.ok(businessUnitService.updateBusinessUnit(id, businessUnitDto));
    }

    /**
     * Deletes the BusinessUnit with the given id
     *
     * Will throw an EntityNotFoundException if there is no BusinessUnit with the given id
     *
     * @param id the id of the BusinessUnit which is to be deleted
     * @return an empty Response with the noContent Status Code
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('change_all_BU_OKRs')")
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
    @PreAuthorize("hasAuthority('read')")
    public PagedModel<EntityModel<BusinessUnitObjectiveDto>> getAllBusinessUnitObjectives(
            @PageableDefault Pageable pageable,
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> end
    ) {
        LocalDate startDate = start.orElse(LocalDate.now().with(firstDayOfYear()));
        LocalDate endDate = end.orElse(LocalDate.now().with(lastDayOfYear()));
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException();
        }
        return pagedResourcesAssemblerObjective.toModel(
                businessUnitObjectiveService.findAllByBusinessUnitId(
                        id,
                        pageable,
                        startDate,
                        endDate
                )
        );
    }

    @PostMapping(
            value = "/{id}/objectives",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('change_all_BU_OKRs') or (hasAuthority('change_own_BU_OKRs') and @customExpressions.belongsToBusinessUnit(#id, principal.username))")
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
    @PreAuthorize("hasAuthority('view_users')")
    public PagedModel<EntityModel<OkrUserDto>> getAllOkrUsers(@PageableDefault Pageable pageable, @PathVariable Long id) {
        if (!businessUnitService.existsById(id)) {
            throw new EntityNotFoundException("No BusinessUnit with this id exists");
        }

        return pagedResourcesAssemblerOkrUser.toModel(okrUserService.findAllByBusinessUnitId(id, pageable));
    }

    @PostMapping(
            value = "/{id}/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('add_users')")
    public ResponseEntity<OkrUserDto> addNewUser(@PathVariable Long id, @RequestBody @Valid OkrUserPostDto newOkrUserDto) {
        if (businessUnitService.existsById(id)) {
            OkrUserDto okrUserDto = okrUserService.insertOkrUserByBusinessUnitId(newOkrUserDto, id);
            return ResponseEntity.created(okrUserDto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(okrUserDto);
        }

        throw new EntityNotFoundException("No BusinessUnit with the given ID exists");
    }

    // endregion
}
