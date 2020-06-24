package com.forexbot.api.dao.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Table(name = "orderDetails")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private int id;

    @NotNull
    @Column(name = "userId")
    private UUID userId;

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
    private String couponCode;

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "createDate")
    private String createDate;

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "status")
    private OrderStatus status;

    public Order() {
        super();
        this.status = OrderStatus.IN_PROCESS;
        this.couponCode = "";
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public int getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getOrderRate() {
        return orderRate;
    }

    public void setOrderRate(double forexRate) {
        this.orderRate = forexRate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getForexAmount() {
        return forexAmount;
    }

    public void setForexAmount(int amount) {
        this.forexAmount = amount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public double getForexTotal() {
        return forexTotal;
    }

    public void setForexTotal(double forexTotal) {
        this.forexTotal = forexTotal;
    }

    public double getGst() {
        return gst;
    }

    public void setGst(double gst) {
        this.gst = gst;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(int salesTotal) {
        this.salesTotal = salesTotal;
    }
}
