package com.paymentchain.customer.controller;

import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer")
public class CustomerController {

    CustomerRepository customerRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public List<Customer> findAll() {
        return  this.customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable long id) {
        Customer customer = this.customerRepository.findById(id).get();
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<Customer>  save(@RequestBody Customer customer) {
        Customer newCustomer = this.customerRepository.save(customer);
        return ResponseEntity.ok(newCustomer);
    }

    @PutMapping
    public ResponseEntity<Customer>  update(@RequestBody Customer customer) {
        Customer newCustomer = this.customerRepository.save(customer);
        return ResponseEntity.ok(newCustomer);
    }


}
