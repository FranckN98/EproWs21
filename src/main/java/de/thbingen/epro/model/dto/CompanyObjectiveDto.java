package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.thbingen.epro.model.business.CompanyObjective;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CompanyObjectiveDto {

    private Long id;
    @Min(value = 0, message = "Achievement must be 0 when creating a new Company Objective")
    @Max(value = 0, message = "Achievement must be 0 when creating a new Company Objective")
    private Integer achievement;
    @NotBlank
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<CompanyKeyResultDto> companyKeyResults = new HashSet<>();
    private LocalDate startDate;
    private LocalDate endDate;

    public CompanyObjectiveDto() {
    }

    public CompanyObjectiveDto(Long id, Integer achievement, String name, Set<CompanyKeyResultDto> companyKeyResults, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.achievement = achievement;
        this.name = name;
        this.companyKeyResults = companyKeyResults;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public Set<CompanyKeyResultDto> getCompanyKeyResults() {
        return companyKeyResults;
    }

    public void setCompanyKeyResults(Set<CompanyKeyResultDto> companyKeyResults) {
        this.companyKeyResults = companyKeyResults;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public static CompanyObjectiveDto from(CompanyObjective companyObjective, Boolean includeKeyResults) {
        CompanyObjectiveDto result = new CompanyObjectiveDto();
        result.id = companyObjective.getId();
        result.achievement = companyObjective.getAchievement();
        result.startDate = companyObjective.getStartDate();
        result.endDate = companyObjective.getEndDate();
        result.name = companyObjective.getName();
        if (includeKeyResults) {
            result.companyKeyResults = companyObjective.getCompanyKeyResults().
                    stream()
                    .map(item -> CompanyKeyResultDto.from(item, false))
                    .collect(Collectors.toSet());
        }
        return result;
    }

    public static CompanyObjectiveDto from(CompanyObjective companyObjective) {
        return from(companyObjective, true);
    }
}
