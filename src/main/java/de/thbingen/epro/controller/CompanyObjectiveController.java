package de.thbingen.epro.controller;

import de.thbingen.epro.exception.NonMatchingIdsException;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.service.CompanyKeyResultService;
import de.thbingen.epro.service.CompanyObjectiveService;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/companyobjectives")
public class CompanyObjectiveController {

    private final CompanyKeyResultService companyKeyResultService;
    private final CompanyObjectiveService companyObjectiveService;
    private final PagedResourcesAssembler<CompanyObjectiveDto> pagedResourcesAssembler;

    public CompanyObjectiveController(CompanyObjectiveService companyObjectiveService, CompanyKeyResultService companyKeyResultService, PagedResourcesAssembler<CompanyObjectiveDto> pagedResourcesAssembler) {
            this.companyObjectiveService = companyObjectiveService;
            this.pagedResourcesAssembler = pagedResourcesAssembler;
            this.companyKeyResultService = companyKeyResultService;
        }

        @GetMapping
        public PagedModel<EntityModel<CompanyObjectiveDto>> findAll (
                @RequestParam(defaultValue = "0") Integer page,
                @RequestParam(defaultValue = "10") Integer size,
                @RequestParam(defaultValue = "id") String sortBy
    ){
            return pagedResourcesAssembler.toModel(companyObjectiveService.getAllCompanyObjectives(page, size, sortBy));
        }

        @PostMapping
        public ResponseEntity<CompanyObjectiveDto> addNew (@RequestBody @Valid CompanyObjectiveDto newCompanyObjective){
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
        public CompanyObjectiveDto findById (@PathVariable Long id){
            Optional<CompanyObjectiveDto> result = companyObjectiveService.findById(id);
            if (result.isPresent()) {
                return result.get();
            }
            throw new EntityNotFoundException("No CompanyObjective with this id exists");
        }

        @PutMapping("/{id}")
        public ResponseEntity<CompanyObjectiveDto> updateById (@PathVariable Long id, @RequestBody CompanyObjectiveDto
        companyObjectiveDto){
            if (companyObjectiveDto.getId() == null) {
                companyObjectiveDto.setId(id);
            }
            if (!Objects.equals(companyObjectiveDto.getId(), id)) {
                throw new NonMatchingIdsException("Ids in path and jsonObject do not match");
            }

            if (!companyObjectiveService.existsById(id)) {
                return this.addNew(companyObjectiveDto);
            }

            return ResponseEntity.ok(companyObjectiveService.saveCompanyObjective(companyObjectiveDto));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteById (@PathVariable Long id){
            if (!companyObjectiveService.existsById(id)) {
                throw new EntityNotFoundException("No CompanyObjective with this id exists");
            }
            companyObjectiveService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        @PostMapping("/{id}/keyresults")
        public ResponseEntity<CompanyKeyResultDto> addNew (@PathVariable Long
        id, @RequestBody @Valid CompanyKeyResultDto newCompanyKeyResultDto){

            CompanyObjectiveDto companyObjectiveDto = companyObjectiveService.findById(id).get();
            CompanyKeyResultDto companyKeyResultDto = companyKeyResultService.saveCompanyKeyResultWithObjective(newCompanyKeyResultDto, companyObjectiveDto);
            UriComponents uriComponents = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host("localhost")
                    .port(8080)
                    .path("/api/v1/companyKeyResults/{id}")
                    .buildAndExpand(companyKeyResultDto.getId());
            return ResponseEntity.created(uriComponents.toUri()).body(companyKeyResultDto);
        }
    }
