package com.example.demo.repository;

import com.example.demo.repository.dao.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForexRepository extends JpaRepository<UserData, Integer> {

    UserData findUserDetailByName(String userName);

    UserData findUserDetailByEmailId(String emailId);

    List<UserData> findAll();

    UserData save(UserData userData);
}