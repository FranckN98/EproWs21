package de.thbingen.epro.controller;

import de.thbingen.epro.ApiError;
import de.thbingen.epro.model.business.CompanyObjective;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.model.mapper.CompanyObjectiveMapper;
import de.thbingen.epro.service.CompanyObjectiveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/companyobjectives")
public class CompanyObjectiveController {

    private final CompanyObjectiveService companyObjectiveService;

    public CompanyObjectiveController(CompanyObjectiveService companyObjectiveService) {
        this.companyObjectiveService = companyObjectiveService;
    }

    @GetMapping
    public List<CompanyObjectiveDto> findAll(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return companyObjectiveService.getAllCompanyObjectives(pageNo, pageSize, sortBy);
    }

    @PostMapping
    public ResponseEntity<CompanyObjectiveDto> addNew(@RequestBody @Valid CompanyObjective newCompanyObjective) {
        CompanyObjectiveDto companyObjectiveDto = companyObjectiveService.saveCompanyObjective(newCompanyObjective);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/api/v1/companyobjectives/{id}")
                .buildAndExpand(companyObjectiveDto.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(companyObjectiveDto);
    }

    @GetMapping("/{id}")
    public CompanyObjectiveDto findById(@PathVariable Long id) {
        Optional<CompanyObjectiveDto> result = companyObjectiveService.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No CompanyObjective with this id exists"
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyObjectiveDto> updateById(@PathVariable Long id, @RequestBody CompanyObjective companyObjective) {
        if (companyObjective.getId() == null) {
            companyObjective.setId(id);
        }
        if (!Objects.equals(companyObjective.getId(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Id in path and id of companyObjective do not match"
            );
        }

        if(!companyObjectiveService.existsById(id)) {
            return this.addNew(companyObjective);
        }

        return ResponseEntity.ok(companyObjectiveService.updateById(companyObjective));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if(!companyObjectiveService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        companyObjectiveService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        for (FieldError error :
                ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors), HttpStatus.BAD_REQUEST);
    }

}
