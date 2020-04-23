package com.example.demo.web.utils;

import java.util.Set;

public class EmailUtil {

    private static final Set<String> allowedDns = Set.of("@gmail", "@outlook", "@yahoo", "@rediff", "@forex");

    public static boolean doLookup(String emailId) {
        return allowedDns.stream().anyMatch(emailId::contains);
    }

    // TODO : add email logic
    public static void sendValidationEmail() {

    }

    // TODO : add email logic
    public static void sendOtpEmail() {

    }
}
