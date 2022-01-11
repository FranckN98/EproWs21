package de.thbingen.epro21.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class BusinessUnit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(targetEntity = BusinessUnitObjective.class, cascade = CascadeType.ALL)
    @JoinColumn(name="business_unit_id")
    private Set<BusinessUnitObjective> businessUnitObjectives = new HashSet<>();

    @OneToMany(targetEntity = OKRUser.class, cascade = CascadeType.ALL)
    @JoinColumn(name="business_unit_id")
    private Set<OKRUser> okrUsers = new HashSet<>();

    public BusinessUnit(String name) {
        this.name = name;
    }

    public BusinessUnit() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<BusinessUnitObjective> getBusinessUnitObjectives() {
        return businessUnitObjectives;
    }
}
