package de.thbingen.epro.model.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.OffsetDateTime;

/*
 * Container Entity for the dataholder, HistoricalCompanyKeyResult
 */
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class CompanyKeyResultHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private OffsetDateTime changeTimeStamp;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ref_id")
    private CompanyKeyResult companyKeyResult;

    @Column(name = "historical_data", nullable = false, columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private HistoricalCompanyKeyResult historicalCompanyKeyResult;

    public CompanyKeyResultHistory() {
    }

    public CompanyKeyResultHistory(Long id, OffsetDateTime changeTimeStamp, CompanyKeyResult companyKeyResult, HistoricalCompanyKeyResult historicalCompanyKeyResult) {
        this.id = id;
        this.changeTimeStamp = changeTimeStamp;
        this.companyKeyResult = companyKeyResult;
        this.historicalCompanyKeyResult = historicalCompanyKeyResult;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getChangeTimeStamp() {
        return changeTimeStamp;
    }

    public void setChangeTimeStamp(OffsetDateTime changeTimeStamp) {
        this.changeTimeStamp = changeTimeStamp;
    }

    public CompanyKeyResult getCompanyKeyResult() {
        return companyKeyResult;
    }

    public void setCompanyKeyResult(CompanyKeyResult companyKeyResult) {
        this.companyKeyResult = companyKeyResult;
    }

    public HistoricalCompanyKeyResult getHistoricalCompanyKeyResult() {
        return historicalCompanyKeyResult;
    }

    public void setHistoricalCompanyKeyResult(HistoricalCompanyKeyResult historicalCompanyKeyResult) {
        this.historicalCompanyKeyResult = historicalCompanyKeyResult;
    }
}
