package com.paymentchain.product.controller;

import com.paymentchain.product.entities.Product;
import com.paymentchain.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    ProductRepository productRepository;


    @GetMapping
    public List<Product> findAll() {
        return  this.productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable long id) {
        Product product = this.productRepository.findById(id).get();
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product>  save(@RequestBody Product product) {
        Product newProduct = this.productRepository.save(product);
        return ResponseEntity.ok(newProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product>  update(@PathVariable long id,@RequestBody Product product) {
        Product newProduct = this.productRepository.save(product);
        return ResponseEntity.ok(newProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Optional<Product> product = this.productRepository.findById(id);
        if(product.isPresent()) {
            this.productRepository.delete(product.get());
        }
        return ResponseEntity.ok().build();
    }
}
