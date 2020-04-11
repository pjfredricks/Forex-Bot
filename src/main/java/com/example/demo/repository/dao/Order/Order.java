package com.example.demo.repository.dao.Order;

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
    @Column(name = "trackingNumber", unique = true)
    private String trackingNumber;

    @NotNull
    @Column(name = "orderType")
    private OrderType orderType;

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "status")
    private OrderStatus status;

    @NotNull
    @Column(name = "amount")
    private Double amount;

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "createDate")
    private String create_date;

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "couponCode")
    private String couponCode;

    // TODO: Add more fields for amount, country..etc

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderType=" + orderType +
                ", status=" + status +
                ", amount=" + amount +
                ", create_date='" + create_date + '\'' +
                ", couponCode='" + couponCode + '\'' +
                '}';
    }
}
