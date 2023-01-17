package com.confluxsys.demo.controller;

import com.confluxsys.demo.exception.ResourceNotFoundException;
import com.confluxsys.demo.model.Order;
import com.confluxsys.demo.repository.CustomerRepository;
import com.confluxsys.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8088")
@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/customers/{customerid}/orders")
    public ResponseEntity<List<Order>> getAllOrdersByCustomerId(@PathVariable(value = "customerid") Long customerid) {
        if (!customerRepository.existsById(customerid)) {
            throw new ResourceNotFoundException("Not found Customer with id = " + customerid);
        }

        List<Order> orders = orderRepository.findByCustomerId(customerid);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrdersByCustomerId(@PathVariable(value = "id") Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Order with id = " + id));

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping("/customers/{customerid}/orders")
    public ResponseEntity<Order> createOrder(@PathVariable(value = "customerid") Long customerid,
                                                 @RequestBody Order orderRequest) {
        Order order = customerRepository.findById(customerid).map(customer -> {
            orderRequest.setCustomer(customer);
            return orderRepository.save(orderRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Customer with id = " + customerid));

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }


    @PutMapping("/orders/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id") long id, @RequestBody Order orderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id " + id + "not found"));

        order.setItem(orderRequest.getItem().toString());
        order.setQuantity(orderRequest.getQuantity());

        return new ResponseEntity<>(orderRepository.save(order), HttpStatus.OK);
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("id") long id) {
        orderRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/customers/{customerid}/orders")
    public ResponseEntity<List<Order>> deleteAllOrdersOfCustomer(@PathVariable(value = "customerid") Long customerid) {
        if (!orderRepository.existsById(customerid)) {
            throw new ResourceNotFoundException("Not found Customer with id = " + customerid);
        }

        orderRepository.deleteByCustomerId(customerid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
