package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.service.BusinessUnitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/businessUnits")
public class BusinessUnitController {

    final BusinessUnitService businessUnitService;

    public BusinessUnitController(BusinessUnitService businessUnitService) {
        this.businessUnitService = businessUnitService;
    }

    @GetMapping
    public List<BusinessUnitDto> findAll() {
        return businessUnitService.findAll();
    }
}
