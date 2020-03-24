package com.example.demo.repository;

import com.example.demo.repository.dao.UserDataRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForexRepository extends JpaRepository<UserDataRequest, Integer> {

    UserDataRequest findUserDetailByName(String userName);

    UserDataRequest findUserDetailByEmailId(String emailId);

    List<UserDataRequest> findAll();

    UserDataRequest save(UserDataRequest userDataRequest);
}