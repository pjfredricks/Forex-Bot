package com.example.demo.service.impl;

import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.OtpDataRepository;
import com.example.demo.repository.UserDataRepository;
import com.example.demo.repository.dao.order.Order;
import com.example.demo.repository.dao.order.OrderType;
import com.example.demo.repository.dao.otp.OtpData;
import com.example.demo.service.SmsService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class SmsServiceImpl implements SmsService {

    public static final String API_KEY = "apikey=y76sJhh/rDc-yHrVdirEXTy8BiOEi23hZKkiewMrcr";
    public static final String SENDER = "&sender=FEXBOT";
    public static final String OTP_MESSAGE = "Your OTP for your Forex Bot transaction is ";
    public static final String ORDER_CONFIRM_MESSAGE = "Hi {userName}, %n" +
            "Your order for {orderType} {currency} {amount} has been confirmed by ForexBot,%n" +
            "Kindly follow the instructions in link below,%n" +
            " forexbot.in/instructions";

    private OtpDataRepository otpDataRepository;
    private OrderRepository orderRepository;
    private UserDataRepository userDataRepository;

    public SmsServiceImpl(OtpDataRepository otpDataRepository, OrderRepository orderRepository, UserDataRepository userDataRepository) {
        this.otpDataRepository = otpDataRepository;
        this.orderRepository = orderRepository;
        this.userDataRepository = userDataRepository;
    }

    @Override
    public void sendOtp(String mobileNum, String otp) throws IOException {
        String message = "&message=" + OTP_MESSAGE + otp;
        String numbers = "&numbers=" + mobileNum;
        String data = API_KEY + numbers + message + SENDER;

        sendSms(mobileNum, data);
    }

    @Override
    public void sendConfirmation(String trackingNumber, String mobileNum) throws IOException {
        String message = ORDER_CONFIRM_MESSAGE;
        Order order = orderRepository.getOrderByTrackingNumber(trackingNumber);
        String name = userDataRepository.getUserDataByMobileNum(mobileNum).getName();

        message = message.replace("{userName}", name.substring(0, Math.min(name.length(), 15)));

        if (OrderType.BUY.equals(order.getOrderType())) {
            message = message.replace("{orderType}", "buying");
        } else {
            message = message.replace("{orderType}", "selling");
        }
        message = message.replace("{currency}", order.getCountryCode());
        message = message.replace("{amount}", String.valueOf(order.getSalesTotal()));

        if (message.length() > 157) {
            throw new RuntimeException("Message length exceeds character limit of 157");
        }
        message = "&message=" + message;
        String numbers = "&numbers=" + mobileNum;

        String data = API_KEY + numbers + message + SENDER;

        sendSms(mobileNum, data);
    }

    private void sendSms(String mobileNum, String data) throws IOException {
        // Send data
        HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
        conn.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));
        final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        final StringBuilder builder = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            builder.append(line);
        }
        rd.close();

        OtpData otpData = otpDataRepository.findOtpDataByMobileNumber(mobileNum);
        if (ObjectUtils.isNotEmpty(otpData)) {
            otpData.setTextLocalResponse(builder.toString());
            otpDataRepository.save(otpData);
        }
    }
}
