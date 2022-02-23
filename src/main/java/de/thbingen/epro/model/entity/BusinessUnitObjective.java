package de.thbingen.epro.model.entity;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BusinessUnitObjective {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer achievement;

    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "business_unit_id")
    private BusinessUnit businessUnit;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_key_result_ref")
    private CompanyKeyResult companyKeyResult;

    private OffsetDateTime startDate = OffsetDateTime.now();
    private OffsetDateTime endDate = OffsetDateTime.now();

    /*@OneToMany(targetEntity = BusinessUnitKeyResult.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_key_result_ref")*/
    @OneToMany(targetEntity = BusinessUnitKeyResult.class, mappedBy = "businessUnitObjective", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BusinessUnitKeyResult> businessUnitKeyResults = new HashSet<>();

    public BusinessUnitObjective() {
    }

    public BusinessUnitObjective(Long id, Integer achievement, String name, BusinessUnit businessUnit, CompanyKeyResult companyKeyResult, OffsetDateTime startDate, OffsetDateTime endDate, Set<BusinessUnitKeyResult> businessUnitKeyResults) {
        this.id = id;
        this.achievement = achievement;
        this.name = name;
        this.businessUnit = businessUnit;
        this.companyKeyResult = companyKeyResult;
        this.startDate = startDate;
        this.endDate = endDate;
        this.businessUnitKeyResults = businessUnitKeyResults;
    }


    public BusinessUnitObjective(Long id, Integer achievement, String name, OffsetDateTime startDate, OffsetDateTime endDate) {
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

    public Set<BusinessUnitKeyResult> getBusinessUnitKeyResults() {
        return businessUnitKeyResults;
    }

    public void setBusinessUnitKeyResults(Set<BusinessUnitKeyResult> businessUnitKeyResults) {
        this.businessUnitKeyResults = businessUnitKeyResults;
    }
}
