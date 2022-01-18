package de.thbingen.epro.model.business;

import javax.persistence.*;
import java.util.Date;

@Entity
public class CompanyKeyResultHistory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private Date changeTimeStamp;

    @Column(
            nullable = false,
            columnDefinition = "TEXT" // insert JSON TYPE here if this Type exist in PostgreSQL
    )
    private String historicalData;

    public CompanyKeyResultHistory(Date changeTimeStamp, String historicalData) {
        this.changeTimeStamp = changeTimeStamp;
        this.historicalData = historicalData;
    }
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ref_id")
    private CompanyKeyResult companyKeyResult ;

    public CompanyKeyResultHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getChangeTimeStamp() {
        return changeTimeStamp;
    }

    public void setChangeTimeStamp(Date changeTimeStamp) {
        this.changeTimeStamp = changeTimeStamp;
    }

    public String getHistoricalData() {
        return historicalData;
    }

    public void setHistoricalData(String historicalData) {
        this.historicalData = historicalData;
    }

    public CompanyKeyResult getCompanyKeyResult() {
        return companyKeyResult;
    }
}
