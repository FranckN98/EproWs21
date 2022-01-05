package de.thbingen.epro21.model;

import javax.persistence.*;

@Entity
@Table(name="businessunit")
public class BusinessUnit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

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
}
