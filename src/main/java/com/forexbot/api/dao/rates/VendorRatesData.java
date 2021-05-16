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
@Table(name = "vendorRatesData")
@Entity
public class VendorRatesData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private int id;

    @NotNull
    @Column(name = "vendorAgentId", unique = true)
    private String vendorAgentId;

    @NotNull
    @Column(name = "vendorName")
    private String vendorName;

    @NotNull
    @Column(name = "ratesData", columnDefinition = "text", length = 25000)
    private String ratesData;

    @NotNull
    @Column(name = "createDate")
    private String createDate;

    @Column(name = "modifiedDate")
    private String modifiedDate;

    @NotNull
    @Column(name = "isLocked")
    private boolean locked = false;
}
