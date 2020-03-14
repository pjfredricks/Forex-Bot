package com.example.demo.repository;

import com.example.demo.repository.dao.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForexRepository extends JpaRepository<UserDetail, Integer> {

    UserDetail findUserDetailByEmailId(String emailId);

    Boolean existsByEmailId(String emailId);

    UserDetail save(UserDetail userDetail);
}