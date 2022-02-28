package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.service.PrivilegeService;
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

/**
 * This Controller is responsible for returning all resources under the /privileges path
 */
@RestController
@RequestMapping("/privileges")
public class PrivilegeController {

    private final PrivilegeService privilegeService;
    private final PagedResourcesAssembler<PrivilegeDto> pagedResourcesAssembler;

    public PrivilegeController(PrivilegeService privilegeService, PagedResourcesAssembler<PrivilegeDto> pagedResourcesAssembler) {
        this.privilegeService = privilegeService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }
    /**
     * Returns all Privileges of the requested Page
     *
     * @param pageable Allows requesting a certain page of a certain size with a certain sort
     * @return The requested Page of Privileges
     */
    @GetMapping(
            produces = MediaTypes.HAL_JSON_VALUE
    )
    public PagedModel<EntityModel<PrivilegeDto>> findAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(privilegeService.findAll(pageable));
    }
    /**
     * Returns the BusinessUnit with the given id
     * <p>
     * Will throw an EntityNotFoundException if there is no Privilege with the given id
     *
     * @param id The id of the requested Privilege
     * @return The requested Privilege
     */
    @GetMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    public PrivilegeDto findById(@PathVariable Long id) {
        Optional<PrivilegeDto> result = privilegeService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No Privilege with this id exists");
    }
    /**
     * Adds the Privilege given in the RequestBody
     * The location header will contain the location at which the new Privilege can be queried
     *
     * @param newPrivilege The Privilege to be added
     * @return the newly added Privilege
     */
    @PostMapping(
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PrivilegeDto> addNew(@RequestBody @Valid PrivilegeDto newPrivilege) {
        PrivilegeDto privilegeDto = privilegeService.insertPrivilege(newPrivilege);
        return ResponseEntity.created(privilegeDto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(privilegeDto);
    }
    /**
     * Update the Privilege with the given id with the Privilege in the Request Body
     *
     * @param id The id of the Privilege to be updated
     * @param privilegeDto The new values for the Privilege
     * @return the newly updated Privilege
     */
    @PutMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PrivilegeDto> updateById(@PathVariable Long id, @RequestBody @Valid PrivilegeDto privilegeDto) {
        if (!privilegeService.existsById(id))
            return this.addNew(privilegeDto);

        return ResponseEntity.ok(privilegeService.updatePrivilege(id, privilegeDto));
    }
    /**
     * Deletes the Privilege with the given id
     * <p>
     * Will throw an EntityNotFoundException if there is no Privilege with the given id
     *
     * @param id The id of the Privilege to be deleted
     * @return An empty Response with the noContent Status Code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!privilegeService.existsById(id))
            throw new EntityNotFoundException("No Privilege with this id exists");
        privilegeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
