package com.forexbot.api.dao.admin;

import java.util.HashMap;
import java.util.Map;

public enum UserCategory {
    ADMIN(0),
    VENDOR(1);

    private final Integer value;
    private static final Map<Integer, UserCategory> map = new HashMap<>();

    UserCategory(Integer value) {
        this.value = value;
    }

    static {
        for (UserCategory orderType : UserCategory.values()) {
            map.put(orderType.value, orderType);
        }
    }

    public static int valueOf(UserCategory orderType) {
        return orderType.value;
    }
}
