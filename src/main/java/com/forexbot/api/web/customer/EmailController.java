package com.forexbot.api.web.customer;

import com.forexbot.api.dao.customer.CustomerRequest;
import com.forexbot.api.service.EmailService;
import com.forexbot.api.service.CustomerService;
import com.forexbot.api.web.utils.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

import static com.forexbot.api.web.utils.Constants.ERROR;
import static com.forexbot.api.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("api/v1")
public class EmailController {

    private final EmailService emailService;
    private final CustomerService customerService;

    public EmailController(EmailService emailService, CustomerService customerService) {
        this.emailService = emailService;
        this.customerService = customerService;
    }

    @PostMapping(path = "/sendResetEmail")
    public ResponseEntity<ResponseWrapper> sendResetEmail(@RequestBody CustomerRequest customerRequest) throws IOException, MessagingException {
        return sendEmail(customerRequest.getEmailId(), EmailService.EmailType.RESET);
    }

    @PostMapping(path = "/sendWelcomeEmail")
    public ResponseEntity<ResponseWrapper> sendWelcomeEmail(@RequestBody CustomerRequest customerRequest) throws IOException, MessagingException {
        return sendEmail(customerRequest.getEmailId(), EmailService.EmailType.WELCOME);
    }

    @PostMapping(path = "/sendVerificationMail")
    public ResponseEntity<ResponseWrapper> sendVerificationMail(@RequestBody CustomerRequest customerRequest) throws IOException, MessagingException {
        return sendEmail(customerRequest.getEmailId(), EmailService.EmailType.VERIFY);
    }

    @PostMapping(path = "/sendConfirmationMail")
    public ResponseEntity<ResponseWrapper> sendConfirmationMail(@RequestBody CustomerRequest customerRequest) throws IOException, MessagingException {
        return sendEmail(customerRequest.getEmailId(), EmailService.EmailType.CONFIRM);
    }

    @PostMapping(path = "/verifyEmail")
    public ResponseEntity<ResponseWrapper> verifyEmail(@RequestBody CustomerRequest customerRequest) {
        try {
            customerService.verifyEmail(customerRequest);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    e.getMessage(),
                    null), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Email verified successfully",
                null), HttpStatus.OK);
    }

    private ResponseEntity<ResponseWrapper> sendEmail(String emailId, EmailService.EmailType emailType) throws IOException, MessagingException {
        try {
            emailService.sendEmail(emailId, emailType);
        } catch (IllegalAccessException ex) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    ex.getMessage(),
                    null), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Email sent successfully",
                null), HttpStatus.OK);
    }
}