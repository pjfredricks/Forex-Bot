package com.forexbot.api.dao.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "orderDetails")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private int id;

    @NotNull
    @Column(name = "customerId")
    private UUID customerId;

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "trackingId", unique = true)
    private String trackingId;

    @NotNull
    @Column(name = "orderType")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @NotNull
    @Column(name = "orderRate")
    private double orderRate;

    @NotNull
    @Column(name = "countryCode")
    private String countryCode;

    @NotNull
    @Column(name = "forexAmount")
    private int forexAmount;

    @NotNull
    @Column(name = "forexTotal")
    private double forexTotal;

    @NotNull
    @Column(name = "gstAmount")
    private double gst;

    @NotNull
    @Column(name = "discountAmount")
    private double discountAmount;

    @NotNull
    @Column(name = "salesTotal")
    private int salesTotal;

    @NotNull
    @Column(name = "couponCode")
    private String couponCode = "";

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "createDate")
    private String createDate;

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "status")
    private OrderStatus status = OrderStatus.IN_PROCESS;
}
