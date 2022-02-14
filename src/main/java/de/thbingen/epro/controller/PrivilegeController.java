package de.thbingen.epro.controller;

import de.thbingen.epro.exception.NonMatchingIdsException;
import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.service.PrivilegeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/privileges")
@PreAuthorize("hasAuthority('access_privileges')")
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    public PrivilegeController(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    @GetMapping
    public List<PrivilegeDto> findAll() {
        return privilegeService.findAll();
    }

    @GetMapping("/{id}")
    public PrivilegeDto findById(@RequestParam Long id) {
        Optional<PrivilegeDto> result = privilegeService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No Privilege with this id exists");
    }

    @PostMapping
    public ResponseEntity<PrivilegeDto> addNew(@RequestBody @Valid PrivilegeDto newPrivilege) {
        PrivilegeDto privilegeDto = privilegeService.savePrivilege(newPrivilege);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port("8080")
                .path("/api/v1/users/{id}")
                .buildAndExpand(privilegeDto.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(privilegeDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrivilegeDto> updateById(@PathVariable Long id, @RequestBody @Valid PrivilegeDto privilegeDto) {
        if (privilegeDto.getId() == null)
            privilegeDto.setId(id);
        if (!Objects.equals(privilegeDto.getId(), id))
            throw new NonMatchingIdsException("Ids in path and jsonObject do not match");
        if (!privilegeService.existsById(id))
            return this.addNew(privilegeDto);

        return ResponseEntity.ok(privilegeService.savePrivilege(privilegeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!privilegeService.existsById(id))
            throw new EntityNotFoundException("No Privilege with this id exists");
        privilegeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
