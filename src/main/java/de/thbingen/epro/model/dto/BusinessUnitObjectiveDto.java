package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.thbingen.epro.model.business.BusinessUnitKeyResult;
import de.thbingen.epro.model.business.BusinessUnitObjective;
import de.thbingen.epro.model.business.CompanyObjective;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BusinessUnitObjectiveDto {
    private Long id;
    @Min(value = 0, message = "Achievement must be 0 when creating a new Company Objective")
    @Max(value = 0, message = "Achievement must be 0 when creating a new Company Objective")
    private Integer achievement;
    @NotBlank
    private String name;

    private OffsetDateTime startDate = OffsetDateTime.now();

    private OffsetDateTime endDate = OffsetDateTime.now();

    private BusinessUnitDto businessUnit;


    private Set<BusinessUnitKeyResultDto> businessUnitKeyResults = new HashSet<>();

    public BusinessUnitObjectiveDto() {
    }

    public BusinessUnitObjectiveDto(Long id, Integer achievement, String name, BusinessUnitDto businessUnit) {
        this.id = id;
        this.achievement = achievement;
        this.name = name;
        this.businessUnit = businessUnit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAchievement() {
        return achievement;
    }

    public void setAchievement(Integer achievement) {
        this.achievement = achievement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BusinessUnitDto getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnitDto businessUnit) {
        this.businessUnit = businessUnit;
    }


    public Set<BusinessUnitKeyResultDto> getBusinessUnitKeyResults() {
        return businessUnitKeyResults;
    }

    public void setBusinessUnitKeyResults(Set<BusinessUnitKeyResultDto> businessUnitKeyResults) {
        this.businessUnitKeyResults = businessUnitKeyResults;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }

}
