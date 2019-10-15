package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.repository.dao.ForexModel;

@Repository
public interface ForexRepository extends JpaRepository<ForexModel, Integer>{
	
}