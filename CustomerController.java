package com.confluxsys.demo.controller;

import com.confluxsys.demo.exception.ResourceNotFoundException;
import com.confluxsys.demo.model.Customer;
import com.confluxsys.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8088")
@RestController
@RequestMapping("/api")
public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers(@RequestParam(required = false) String name) {
        List<Customer> customers = new ArrayList<Customer>();

        if (name == null)
            customerRepository.findAll().forEach(customers::add);
        else
            customerRepository.findByNameContaining(name).forEach(customers::add);

        if (customers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Customer with id = " + id));

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer _customer = customerRepository.save(new Customer(customer.getName(), customer.getEmail(), customer.getNumber()));
        return new ResponseEntity<>(_customer, HttpStatus.CREATED);
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer) {
        Customer _customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found customer with id = " + id));

        _customer.setName(customer.getName());
        _customer.setEmail(customer.getEmail());
        _customer.setNumber(customer.getNumber());

        return new ResponseEntity<>(customerRepository.save(_customer), HttpStatus.OK);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable("id") long id) {
        customerRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/customers")
    public ResponseEntity<HttpStatus> deleteAllCustomers() {
        customerRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
