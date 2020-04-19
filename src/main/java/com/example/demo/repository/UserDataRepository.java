package com.example.demo.repository;

import com.example.demo.repository.dao.UserData.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Integer> {

    UserData getUserDataByName(String userName);

    UserData getUserDataByEmailIdOrMobileNum(String emailId, String mobileNum);

    UserData getUserDataByUserId(UUID userId);

    List<UserData> findAll();

    UserData save(UserData userData);
}