package com.forexbot.api.dao.rates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorRatesDTO {
    private String vendorAgentId;
    private String vendorName;
    private List<ForexData> forexData;
    @ApiModelProperty(hidden = true)
    private String createDate;
    @ApiModelProperty(hidden = true)
    private String modifiedDate;
    @ApiModelProperty(hidden = true)
    private boolean locked;
}
