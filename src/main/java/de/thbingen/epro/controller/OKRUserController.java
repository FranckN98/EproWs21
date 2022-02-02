package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.OKRUserDto;
import de.thbingen.epro.service.OKRUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class OKRUserController {

    final OKRUserService okrUserService;

    public OKRUserController(OKRUserService okrUserService) {
        this.okrUserService = okrUserService;
    }

    @GetMapping
    public List<OKRUserDto> findAll() {
        return okrUserService.findAll();
    }

    @GetMapping("/{id}")
    public OKRUserDto findById(@PathVariable Long id) {
        Optional<OKRUserDto> result = okrUserService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No User with this id exists");
    }

    @PostMapping
    public ResponseEntity<OKRUserDto> addNew(@RequestBody @Valid OKRUserDto newUser) {
        OKRUserDto okrUserDto = okrUserService.saveOKRUser(newUser);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/api/v1/users/{id}")
                .buildAndExpand(okrUserDto.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(okrUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!okrUserService.existsById(id)) {
            throw new EntityNotFoundException("No OKRUser with this id exists");
        }
        okrUserService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
