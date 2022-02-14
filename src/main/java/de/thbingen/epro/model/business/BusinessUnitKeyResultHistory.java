package de.thbingen.epro.model.business;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class BusinessUnitKeyResultHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private OffsetDateTime changeTimeStamp;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ref_id")
    private BusinessUnitKeyResult currentBusinessUnitKeyResult;

    @Column(name = "historical_data", nullable = false, columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private HistoricalBusinessUnitKeyResult historicalBusinessUnitKeyResult;

    @Transient
    private BusinessUnitKeyResult businessUnitKeyResultHistorical;

    public BusinessUnitKeyResultHistory(Long id, BusinessUnitKeyResult currentBusinessUnitKeyResult, OffsetDateTime changeTimeStamp, HistoricalBusinessUnitKeyResult businessUnitKeyResultHistorical) {
        this.id = id;
        this.currentBusinessUnitKeyResult = currentBusinessUnitKeyResult;
        this.changeTimeStamp = changeTimeStamp;
        this.historicalBusinessUnitKeyResult = businessUnitKeyResultHistorical;
    }

    public BusinessUnitKeyResultHistory() {
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

    public BusinessUnitKeyResult getCurrentBusinessUnitKeyResult() {
        return currentBusinessUnitKeyResult;
    }

    public void setCurrentBusinessUnitKeyResult(BusinessUnitKeyResult businessUnitKeyResultOrigin) {
        this.currentBusinessUnitKeyResult = businessUnitKeyResultOrigin;
    }

    @JsonIgnore
    public HistoricalBusinessUnitKeyResult getHistoricalBusinessUnitKeyResult() {
        return historicalBusinessUnitKeyResult;
    }

    @JsonProperty
    public void setHistoricalBusinessUnitKeyResult(HistoricalBusinessUnitKeyResult businessUnitKeyResultHistorical) {
        this.historicalBusinessUnitKeyResult = businessUnitKeyResultHistorical;
    }

    public BusinessUnitKeyResult getBusinessUnitKeyResultHistorical() {
        return businessUnitKeyResultHistorical;
    }

    public void setBusinessUnitKeyResultHistorical(BusinessUnitKeyResult businessUnitKeyResultHistorical) {
        this.businessUnitKeyResultHistorical = businessUnitKeyResultHistorical;
    }
}
