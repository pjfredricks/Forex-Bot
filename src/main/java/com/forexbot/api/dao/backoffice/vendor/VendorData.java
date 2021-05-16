package com.forexbot.api.dao.backoffice.vendor;

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
@Table(name = "vendorData")
@Entity
public class VendorData {

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
    @Column(name = "gstNumber")
    private String gstNumber;

    @NotNull
    @Column(name = "rbiagentId")
    private String rbiagentId;

    @NotNull
    @Column(name = "panNumber")
    private String panNumber;
}
