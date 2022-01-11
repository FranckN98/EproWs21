package de.thbingen.epro21.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BusinessUnitObjective
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer achievement;

    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "business_unit_id")
    private  BusinessUnit businessUnit;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_key_result_id")
    private  CompanyKeyResult companyKeyResult;

    @OneToMany(targetEntity = BusinessUnitKeyResult.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "business_unit_objective_id")
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

    public Set<BusinessUnitKeyResult> getBusinessUnitKeyResults() {
        return businessUnitKeyResults;
    }

    public CompanyKeyResult getCompanyKeyResult() {
        return companyKeyResult;
    }
}
