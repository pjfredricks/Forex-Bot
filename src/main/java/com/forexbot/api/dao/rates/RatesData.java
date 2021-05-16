package com.forexbot.api.dao.rates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "ratesData")
@Entity
public class RatesData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private int id;

    @NotNull
    @Column(name = "ratesDetails", columnDefinition = "text", length = 25000)
    private String ratesDetails;

    @NotNull
    @Column(name = "createDate")
    private String createDate;

    @NotNull
    @Column(name = "approvedBy")
    private String approvedBy;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RatesStatus status;

    public String getRatesDetails() {
        return ratesDetails;
    }

    public void setRatesDetails(String ratesDetails) {
        this.ratesDetails = ratesDetails;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public RatesStatus getStatus() {
        return status;
    }

    public void setStatus(RatesStatus status) {
        this.status = status;
    }
}
