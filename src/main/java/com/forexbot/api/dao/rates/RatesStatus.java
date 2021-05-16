package com.forexbot.api.dao.rates;

import java.util.HashMap;
import java.util.Map;

public enum RatesStatus {
    OPEN(0),
    ACTIVE(1),
    CLOSED(2);

    private final Integer value;
    private static final Map<Integer, RatesStatus> map = new HashMap<>();

    RatesStatus(Integer value) {
        this.value = value;
    }

    static {
        for (RatesStatus orderType : RatesStatus.values()) {
            map.put(orderType.value, orderType);
        }
    }

    public static RatesStatus valueOf(int orderType) {
        return map.get(orderType);
    }
}
