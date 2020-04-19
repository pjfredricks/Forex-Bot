package com.example.demo.repository.dao.Order;

import java.util.HashMap;
import java.util.Map;

public enum OrderType {
    BUY(0),
    SELL(1);

    private int value;
    private static Map map = new HashMap();

    OrderType(int value) {
        this.value = value;
    }

    static {
        for (OrderType orderType : OrderType.values()) {
            map.put(orderType.value, orderType);
        }
    }

    public static OrderType valueOf(int orderType) {
        return (OrderType) map.get(orderType);
    }

    public int getValue() {
        return value;
    }
}
