package de.thbingen.epro.model.business;

import javax.persistence.*;

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
    @JoinColumn(name = "company_key_result_ref")
    private  CompanyKeyResult companyKeyResult;

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

    public CompanyKeyResult getCompanyKeyResult() {
        return companyKeyResult;
    }
}
