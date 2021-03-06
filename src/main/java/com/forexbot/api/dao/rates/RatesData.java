package com.forexbot.api.dao.rates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
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
    private RatesStatus status = RatesStatus.OPEN;
}
