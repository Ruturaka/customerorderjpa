package com.confluxsys.demo.repository;

import com.confluxsys.demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
    List<Customer> findByNameContaining(String name);
}
