package com.forexbot.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forexbot.api.dao.customer.CustomerDataResponse;
import com.forexbot.api.dao.otp.OtpData;
import com.forexbot.api.dao.otp.OtpRequest;
import com.forexbot.api.dao.otp.OtpType;
import com.forexbot.api.dao.customer.CustomerData;
import com.forexbot.api.dao.customer.CustomerRequest;
import com.forexbot.api.dao.customer.CustomerResponse;
import com.forexbot.api.repository.BackOfficeRepository;
import com.forexbot.api.repository.OtpDataRepository;
import com.forexbot.api.repository.CustomerRepository;
import com.forexbot.api.service.CustomerService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.forexbot.api.web.utils.Constants.ZONE;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final OtpDataRepository otpDataRepository;
    private final CustomerRepository customerRepository;
    private final BackOfficeRepository backOfficeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ObjectMapper mapper;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               BCryptPasswordEncoder bCryptPasswordEncoder,
                               BackOfficeRepository backOfficeRepository,
                               OtpDataRepository otpDataRepository) {
        this.customerRepository = customerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.otpDataRepository = otpDataRepository;
        this.backOfficeRepository = backOfficeRepository;
        this.mapper = new ObjectMapper();
    }

    @Override
    public List<CustomerDataResponse> getAllCustomers() {
        List<CustomerData> customerDataList = customerRepository.findAll();
        customerDataList.removeIf(customerData -> !customerData.isActive());
        return customerDataList.stream()
                .map(customerData -> convertDataToDataResponse(customerData))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerResponse signUpCustomer(CustomerRequest customerRequest) throws IllegalAccessException {
        if (isOldDataActive(customerRequest)) {
            throw new IllegalAccessException("Old Data is still active for email and mobileNum");
        }

        CustomerData customerData = mapRequestToData(customerRequest);
        OtpData otpData = otpDataRepository.findOtpDataByMobileNum(customerRequest.getMobileNum());
        if (ObjectUtils.isNotEmpty(otpData) && otpData.isOtpVerified()) {
            customerData.setMobileVerified(true);
        }
        customerRepository.save(customerData);

        return mapDataToResponse(customerData);
    }

    @Override
    public CustomerResponse login(CustomerRequest customerRequest) {
        CustomerData customerData = null;

        if (customerRequest.getEmailId().contains("@")) {
            customerData = getCustomerDataByEmailIdOrMobileNum(customerRequest.getEmailId(), null);
        } else {
            customerData = getCustomerDataByEmailIdOrMobileNum(null, customerRequest.getEmailId());
        }

        if (ObjectUtils.isNotEmpty(customerData) && checkPasswordsMatch(customerRequest.getPassword(), customerData.getPassword())) {
            return mapDataToResponse(customerData);
        }
        return null;
    }

    @Override
    public CustomerDataResponse getCustomerIdDetailsById(UUID customerId) {
        CustomerData customerData = customerRepository.getCustomerDataByCustomerId(customerId);
        return convertDataToDataResponse(customerData);
    }

    private CustomerDataResponse convertDataToDataResponse(CustomerData customerData) {
        customerData.setPassword(new String(Hex.decode(customerData.getHexData())));
        return mapper.convertValue(customerData, CustomerDataResponse.class);
    }

    public CustomerResponse resetCustomerPassword(CustomerRequest resetRequest) throws IllegalAccessException {
        CustomerData customerData = customerRepository.getCustomerDataByCustomerId(UUID.fromString(resetRequest.getCustomerId()));

        if (ObjectUtils.isNotEmpty(customerData)) {
            customerData.setHexData(String.valueOf(Hex.encode(resetRequest.getPassword().getBytes())));
            customerData.setPassword(bCryptPasswordEncoder.encode(resetRequest.getPassword()));
            customerData.setModifiedDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
            customerData.setModifiedBy(resetRequest.getModifiedBy());
            customerRepository.save(customerData);
            return mapDataToResponse(customerData);
        }

        throw new IllegalAccessException("Bad Credentials, Password reset failed");
    }

    @Override
    public CustomerData getCustomerDataByEmailIdOrMobileNum(String emailId, String mobileNum) {
        return customerRepository.getCustomerDataByEmailIdOrMobileNumAndIsActive(emailId, mobileNum);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomerDetails(CustomerRequest updateRequest) throws IllegalAccessException {
        boolean isAdmin = backOfficeRepository.existsByUserId(updateRequest.getModifiedBy());
        CustomerData customerData = customerRepository.getCustomerDataByCustomerId(UUID.fromString(updateRequest.getCustomerId()));

        if (ObjectUtils.isNotEmpty(customerData)) {
            if (isAdmin) {
                customerData.setEmailId(updateRequest.getEmailId());
                customerData.setMobileNum(updateRequest.getMobileNum());
            } else {
                validateAndSetMobileandEmail(updateRequest, customerData);
            }
            if (StringUtils.isNotBlank(updateRequest.getName())) {
                customerData.setName(updateRequest.getName());
            }
            customerData.setModifiedDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
            customerData.setModifiedBy(updateRequest.getModifiedBy());
            customerRepository.save(customerData);
            return mapDataToResponse(customerData);
        }

        throw new IllegalAccessException("Not able to find records for requested details");
    }

    @Override
    @Transactional
    public String generateAndSaveOtp(OtpRequest otpRequest) throws IllegalAccessException {
        String otp = RandomStringUtils.randomNumeric(6);
        isNewCustomer(otpRequest);

        OtpData otpData = otpDataRepository.findOtpDataByMobileNumAndOtpType(otpRequest.getMobileNum(),
                OtpType.valueOf(otpRequest.getOtpType()));

        otpData = constructOtpData(otpData, otp, otpRequest);

        otpDataRepository.save(otpData);
        return otp;
    }

    @Override
    @Transactional
    public boolean verifyOtp(OtpRequest otpRequest) throws IllegalAccessException {
        OtpData otpData = otpDataRepository.findOtpDataByMobileNumAndOtpType(otpRequest.getMobileNum(),
                OtpType.valueOf(otpRequest.getOtpType()));

        if (ObjectUtils.isEmpty(otpData)) {
            throw new IllegalAccessException("Invalid emailId or Otp Type");
        }

        if (otpRequest.getOtpType() != 0) {
            if (ObjectUtils.isEmpty(customerRepository.getCustomerDataByCustomerId(UUID.fromString(otpRequest.getCustomerId())))) {
                throw new IllegalAccessException("No customer found for customerId: " + otpRequest.getCustomerId());
            }
        }

        if (bCryptPasswordEncoder.matches(otpRequest.getOtp(), otpData.getOtp())) {
            otpData.setOtpVerified(true);
            otpDataRepository.save(otpData);
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public void verifyEmail(CustomerRequest customerRequest) throws IllegalAccessException {
        CustomerData customerData = customerRepository.getCustomerDataByCustomerId(UUID.fromString(customerRequest.getCustomerId()));
        if (ObjectUtils.isEmpty(customerData)) {
            throw new IllegalAccessException("No customer found for customerId: " + customerRequest.getCustomerId());
        }
        customerData.setEmailVerified(true);
        customerRepository.save(customerData);
    }

    @Override
    @Transactional
    public void deleteCustomer(UUID customerId, String deletedBy) {
        CustomerData customerData = customerRepository.getCustomerDataByCustomerId(customerId);
        customerData.setActive(false);
        customerData.setDeletedBy(deletedBy);
        customerRepository.save(customerData);
    }

    private void isNewCustomer(OtpRequest otpRequest) throws IllegalAccessException {
        if (OtpType.SIGN_UP.equals(OtpType.valueOf(otpRequest.getOtpType()))
                && customerRepository.existsByMobileNumOrEmailId(otpRequest.getMobileNum(), otpRequest.getEmailId())) {
            throw new IllegalAccessException("Customer already registered with credentials");
        }
    }

    private OtpData constructOtpData(OtpData otpData, String otp, OtpRequest otpRequest) {
        if (ObjectUtils.isNotEmpty(otpData)) {
            otpData.setOtp(bCryptPasswordEncoder.encode(otp));
            otpData.setModifiedDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
            otpData.setRetryCount(otpData.getRetryCount() + 1);
        } else {
            otpData = new OtpData();
            otpData.setOtp(bCryptPasswordEncoder.encode(otp));
            otpData.setMobileNum(otpRequest.getMobileNum());
            otpData.setOtpType(OtpType.valueOf(otpRequest.getOtpType()));
            otpData.setCreateDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
            otpData.setRetryCount(0);
        }

        return otpData;
    }

    private boolean checkPasswordsMatch(String enteredPassword, String passwordFromDb) {
        return bCryptPasswordEncoder.matches(enteredPassword, passwordFromDb);
    }

    private void validateAndSetMobileandEmail(CustomerRequest updateRequest, CustomerData customerData) throws IllegalAccessException {
        if (StringUtils.isNotBlank(updateRequest.getEmailId()) && customerData.isEmailVerified()) {
            throw new IllegalAccessException("EmailId is verified, Not allowed to update");
        } else {
            customerData.setEmailId(updateRequest.getEmailId());
        }
        if (StringUtils.isNotBlank(updateRequest.getMobileNum()) && customerData.isMobileVerified()) {
            throw new IllegalAccessException("MobileNum is verified, Not allowed to update");
        } else {
            customerData.setMobileNum(updateRequest.getMobileNum());
        }
    }

    private CustomerData mapRequestToData(CustomerRequest customerRequest) {
        CustomerData customerData = new CustomerData();

        customerData.setCustomerId(UUID.randomUUID());
        customerData.setEmailId(customerRequest.getEmailId());
        customerData.setMobileNum(customerRequest.getMobileNum());
        customerData.setName(customerRequest.getName());
        customerData.setHexData(String.valueOf(Hex.encode(customerRequest.getPassword().getBytes())));
        customerData.setPassword(bCryptPasswordEncoder.encode(customerRequest.getPassword()));
        customerData.setCreateDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
        return customerData;
    }

    private CustomerResponse mapDataToResponse(CustomerData customerData) {
        CustomerResponse response = new CustomerResponse();

        response.setCustomerId(customerData.getCustomerId().toString());
        response.setEmailId(customerData.getEmailId());
        response.setMobileNum(customerData.getMobileNum());
        response.setName(customerData.getName());
        return response;
    }

    private boolean isOldDataActive(CustomerRequest customerRequest) {
        CustomerData customerData = customerRepository
                .getCustomerDataByEmailIdOrMobileNum(customerRequest.getEmailId(),
                        customerRequest.getMobileNum());
        if (ObjectUtils.isNotEmpty(customerData)) {
            return customerData.isActive();
        }
        return false;
    }
}