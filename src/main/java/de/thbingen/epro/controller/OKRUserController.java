package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.service.OkrUserService;
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
public class OkrUserController {

    final OkrUserService OkrUserService;

    public OkrUserController(OkrUserService OkrUserService) {
        this.OkrUserService = OkrUserService;
    }

    @GetMapping
    public List<OkrUserDto> findAll() {
        return OkrUserService.findAll();
    }

    @GetMapping("/{id}")
    public OkrUserDto findById(@PathVariable Long id) {
        Optional<OkrUserDto> result = OkrUserService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No User with this id exists");
    }

    @PostMapping
    public ResponseEntity<OkrUserDto> addNew(@RequestBody @Valid OkrUserDto newUser) {
        OkrUserDto OkrUserDto = OkrUserService.saveOkrUser(newUser);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/api/v1/users/{id}")
                .buildAndExpand(OkrUserDto.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(OkrUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!OkrUserService.existsById(id)) {
            throw new EntityNotFoundException("No OkrUser with this id exists");
        }
        OkrUserService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
