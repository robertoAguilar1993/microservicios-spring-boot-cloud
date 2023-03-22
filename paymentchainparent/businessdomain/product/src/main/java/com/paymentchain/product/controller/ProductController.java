package com.paymentchain.product.controller;

import com.paymentchain.product.entities.Product;
import com.paymentchain.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Value("${user.role}")
    private String role;

    @Value("${user.alias}")
    private String alias;

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        System.out.print("el role es : " +role);
        System.out.print("el alias es : " +alias);
        List<Product> products = this.productRepository.findAll();
        if(CollectionUtils.isEmpty(products)){
            return ResponseEntity.noContent().build();
        }
        return  ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable long id) {
       return this.productRepository.findById(id)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product>  save(@RequestBody Product product) {
        Product newProduct = this.productRepository.save(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
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
