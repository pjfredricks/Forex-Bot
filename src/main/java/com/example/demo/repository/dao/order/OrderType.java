package com.example.demo.repository.dao.order;

import java.util.HashMap;
import java.util.Map;

public enum OrderType {
    BUY(0),
    SELL(1);

    private final Integer value;
    private static final Map<Integer, OrderType> map = new HashMap<>();

    OrderType(Integer value) {
        this.value = value;
    }

    static {
        for (OrderType orderType : OrderType.values()) {
            map.put(orderType.value, orderType);
        }
    }

    public static OrderType valueOf(int orderType) {
        return map.get(orderType);
    }

    public int getValue() {
        return value;
    }
}
