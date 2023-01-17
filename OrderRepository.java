package com.confluxsys.demo.repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import com.confluxsys.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long>{

    List<Order> findByCustomerId(Long postId);

    @Transactional
    void deleteByCustomerId(long customerid);
}
