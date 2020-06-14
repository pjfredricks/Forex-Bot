package com.forexbot.api.dao.otp;

import java.util.HashMap;
import java.util.Map;

public enum OtpType {
    SIGN_UP(0),
    RESET(1),
    ORDER_CONFIRM(2);


    private final Integer value;
    private static final Map<Integer, OtpType> map = new HashMap<>();

    OtpType(int value) {
        this.value = value;
    }

    static {
        for (OtpType otpType : OtpType.values()) {
            map.put(otpType.value, otpType);
        }
    }

    public static OtpType valueOf(int otpType) {
        return map.get(otpType);
    }

    public int getValue() {
        return value;
    }
}
