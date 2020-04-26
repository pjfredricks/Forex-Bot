package com.example.demo.repository.dao.order;

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
    @Column(name = "trackingNumber", unique = true)
    private String trackingNumber;

    @NotNull
    @Column(name = "orderType")
    private OrderType orderType;

    @NotNull
    @Column(name = "countryCode")
    private String countryCode;

    @NotNull
    @Column(name = "forexAmount")
    private double forexAmount;

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

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public double getForexAmount() {
        return forexAmount;
    }

    public void setForexAmount(double amount) {
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

    @Override
    public String toString() {
        return "order{" +
                "userId=" + userId +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", orderType=" + orderType +
                ", countryCode='" + countryCode + '\'' +
                ", forexAmount=" + forexAmount +
                ", forexTotal=" + forexTotal +
                ", gst=" + gst +
                ", discountAmount=" + discountAmount +
                ", salesTotal=" + salesTotal +
                ", couponCode='" + couponCode + '\'' +
                ", createDate='" + createDate + '\'' +
                ", status=" + status +
                '}';
    }
}
