package com.bushy.springbatchexample.repository;

import com.bushy.springbatchexample.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository <Employee, Integer> {

}
