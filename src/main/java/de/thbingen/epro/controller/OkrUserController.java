package de.thbingen.epro.controller;

import de.thbingen.epro.exception.NonMatchingIdsException;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.service.OkrUserService;
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
@RequestMapping("/users")
public class OkrUserController {

    private final OkrUserService okrUserService;

    public OkrUserController(OkrUserService okrUserService) {
        this.okrUserService = okrUserService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('view_users')")
    public List<OkrUserDto> findAll() {
        return okrUserService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('view_users') or (hasAuthority('read') and @customExpressions.isSameUser(#id, principal.username))")
    public OkrUserDto findById(@PathVariable Long id) {
        Optional<OkrUserDto> result = okrUserService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No User with this id exists");
    }

    @PostMapping
    @PreAuthorize("hasAuthority('add_users')")
    public ResponseEntity<OkrUserDto> addNew(@RequestBody @Valid OkrUserDto newUser) {
        OkrUserDto okrUserDto = okrUserService.saveOkrUser(newUser);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/api/v1/users/{id}")
                .buildAndExpand(okrUserDto.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(okrUserDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('change_users')")
    public ResponseEntity<OkrUserDto> updateById(@PathVariable Long id, @RequestBody @Valid OkrUserDto okrUserDto) {
        if (okrUserDto.getId() == null)
            okrUserDto.setId(id);
        if (!Objects.equals(okrUserDto.getId(), id))
            throw new NonMatchingIdsException("Ids in path and jsonObject do not match");
        if (!okrUserService.existsById(id))
            return this.addNew(okrUserDto);

        return ResponseEntity.ok(okrUserService.saveOkrUser(okrUserDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('change_users')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!okrUserService.existsById(id)) {
            throw new EntityNotFoundException("No OkrUser with this id exists");
        }
        okrUserService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
