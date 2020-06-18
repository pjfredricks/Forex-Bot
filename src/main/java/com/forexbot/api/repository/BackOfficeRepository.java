package com.forexbot.api.repository;

import com.forexbot.api.dao.admin.BackOfficeUserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackOfficeRepository extends JpaRepository<BackOfficeUserData, Integer> {

    List<BackOfficeUserData> findAll();

    BackOfficeUserData save(BackOfficeUserData adminData);

    BackOfficeUserData getBackOfficeUserDataByEmailId(String emailId);
}