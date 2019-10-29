package com.cw.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cw.model.Employee;

@Repository
public interface EmployeeDao extends MongoRepository<Employee, String> {

}
