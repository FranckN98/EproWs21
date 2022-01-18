package de.thbingen.epro.model.business;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ref_id")
    private BusinessUnitKeyResult currentBusinessUnitKeyResult;

    @Column(nullable = false)
    private OffsetDateTime changeTimeStamp;

    @Column(name = "historical_data", nullable = false, columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private BusinessUnitKeyResultHistoryDto businessUnitKeyResultHistoricalDto;

    @Transient
    private BusinessUnitKeyResult businessUnitKeyResultHistorical;

    public BusinessUnitKeyResultHistory(Long id, BusinessUnitKeyResult currentBusinessUnitKeyResult, OffsetDateTime changeTimeStamp, BusinessUnitKeyResultHistoryDto businessUnitKeyResultHistorical) {
        this.id = id;
        this.currentBusinessUnitKeyResult = currentBusinessUnitKeyResult;
        this.changeTimeStamp = changeTimeStamp;
        this.businessUnitKeyResultHistoricalDto = businessUnitKeyResultHistorical;
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
    public BusinessUnitKeyResultHistoryDto getBusinessUnitKeyResultHistoricalDto() {
        return businessUnitKeyResultHistoricalDto;
    }

    @JsonProperty
    public void setBusinessUnitKeyResultHistoricalDto(BusinessUnitKeyResultHistoryDto businessUnitKeyResultHistorical) {
        this.businessUnitKeyResultHistoricalDto = businessUnitKeyResultHistorical;
    }

    public BusinessUnitKeyResult getBusinessUnitKeyResultHistorical() {
        return businessUnitKeyResultHistorical;
    }

    public void setBusinessUnitKeyResultHistorical(BusinessUnitKeyResult businessUnitKeyResultHistorical) {
        this.businessUnitKeyResultHistorical = businessUnitKeyResultHistorical;
    }
}
