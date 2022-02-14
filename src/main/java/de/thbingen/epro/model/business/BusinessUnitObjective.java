package de.thbingen.epro.model.business;

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

    private OffsetDateTime startDate;

    private OffsetDateTime endDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "business_unit_id")
    private BusinessUnit businessUnit;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_key_result_ref")
    private CompanyKeyResult companyKeyResult;


    @OneToMany(targetEntity = BusinessUnitKeyResult.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_key_result_ref")
    private Set<BusinessUnitKeyResult> businessUnitKeyResults = new HashSet<>();

    public BusinessUnitObjective(Integer achievement, String name) {
        this.achievement = achievement;
        this.name = name;
    }

    public BusinessUnitObjective() {
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

    public void setCompanyKeyResult(CompanyKeyResult companyKeyResult) {
        this.companyKeyResult = companyKeyResult;
    }

    public void setBusinessUnitKeyResults(Set<BusinessUnitKeyResult> businessUnitKeyResults) {
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

    public CompanyKeyResult getCompanyKeyResult() {
        return companyKeyResult;
    }

    public Set<BusinessUnitKeyResult> getBusinessUnitKeyResults() {
        return businessUnitKeyResults;
    }
}
