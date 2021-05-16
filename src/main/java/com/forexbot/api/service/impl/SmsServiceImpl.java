package com.forexbot.api.service.impl;

import com.forexbot.api.dao.order.Order;
import com.forexbot.api.dao.order.OrderType;
import com.forexbot.api.dao.otp.OtpData;
import com.forexbot.api.repository.OrderRepository;
import com.forexbot.api.repository.OtpDataRepository;
import com.forexbot.api.repository.CustomerRepository;
import com.forexbot.api.service.SmsService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class SmsServiceImpl implements SmsService {

    public static final String API_KEY = "apikey=y76sJhh/rDc-yHrVdirEXTy8BiOEi23hZKkiewMrcr";
    public static final String SENDER = "&sender=FEXBOT";
    public static final String OTP_MESSAGE = "Your OTP for your Forex Bot transaction is ";
    public static final String ORDER_CONFIRM_MESSAGE = "Hi {userName}, %n" +
            "Your order for {orderType} {currency} {amount} has been confirmed by ForexBot,%n" +
            "Kindly follow the instructions in link below,%n" +
            " forexbot.in/instructions";

    private final OtpDataRepository otpDataRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public SmsServiceImpl(OtpDataRepository otpDataRepository, OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.otpDataRepository = otpDataRepository;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void sendOtp(String mobileNum, String otp) throws IOException {
        String message = "&message=" + OTP_MESSAGE + otp;
        String numbers = "&numbers=" + mobileNum;
        String data = API_KEY + numbers + message;

        sendSms(mobileNum, data);
    }

    @Override
    public void sendConfirmation(String trackingId, String mobileNum) throws IOException {
        String message = ORDER_CONFIRM_MESSAGE;
        Order order = orderRepository.getOrderByTrackingId(trackingId);
        String name = customerRepository.getCustomerDataByMobileNum(mobileNum).getName();

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

        Optional<OtpData> otpData = otpDataRepository.findOtpDataByMobileNum(mobileNum);
        if (otpData.isPresent()) {
            otpData.get().setTextLocalResponse(builder.toString());
            otpDataRepository.save(otpData.get());
        }
    }
}
