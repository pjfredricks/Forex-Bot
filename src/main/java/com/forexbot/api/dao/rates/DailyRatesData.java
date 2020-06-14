package com.forexbot.api.dao.rates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(name = "ratesData")
@Entity
public class DailyRatesData {

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
    private LocalDateTime createDate;

    @NotNull
    @Column(name = "triggeredBy")
    private String triggeredBy;

    public String getRatesDetails() {
        return ratesDetails;
    }

    public void setRatesDetails(String ratesDetails) {
        this.ratesDetails = ratesDetails;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }
}