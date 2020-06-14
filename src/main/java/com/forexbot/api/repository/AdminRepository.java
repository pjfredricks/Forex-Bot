package com.forexbot.api.repository;

import com.forexbot.api.dao.admin.AdminData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<AdminData, Integer> {
    List<AdminData> findAll();

    AdminData save(AdminData adminData);

    AdminData getAdminDataByUserName(String userName);
}