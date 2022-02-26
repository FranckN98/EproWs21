package de.thbingen.epro.model.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BusinessUnitObjective {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Float achievement;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "business_unit_id")
    private BusinessUnit businessUnit;

    @ManyToOne
    @JoinColumn(name = "company_key_result_ref")
    private CompanyKeyResult companyKeyResult;

    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(
            targetEntity = BusinessUnitKeyResult.class,
            mappedBy = "businessUnitObjective",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private Set<BusinessUnitKeyResult> businessUnitKeyResults = new HashSet<>();

    public BusinessUnitObjective() {
    }

    public BusinessUnitObjective(Long id, Float achievement, String name, BusinessUnit businessUnit, CompanyKeyResult companyKeyResult, LocalDate startDate, LocalDate endDate, Set<BusinessUnitKeyResult> businessUnitKeyResults) {
        this.id = id;
        this.achievement = achievement;
        this.name = name;
        this.businessUnit = businessUnit;
        this.companyKeyResult = companyKeyResult;
        this.startDate = startDate;
        this.endDate = endDate;
        this.businessUnitKeyResults = businessUnitKeyResults;
    }


    public BusinessUnitObjective(Long id, Float achievement, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.achievement = achievement;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getAchievement() {
        return achievement;
    }

    public void setAchievement(Float achievement) {
        this.achievement = achievement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public CompanyKeyResult getCompanyKeyResult() {
        return companyKeyResult;
    }

    public void setCompanyKeyResult(CompanyKeyResult companyKeyResult) {
        this.companyKeyResult = companyKeyResult;
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

    public Set<BusinessUnitKeyResult> getBusinessUnitKeyResults() {
        return businessUnitKeyResults;
    }

    public void setBusinessUnitKeyResults(Set<BusinessUnitKeyResult> businessUnitKeyResults) {
        this.businessUnitKeyResults = businessUnitKeyResults;
    }
}
