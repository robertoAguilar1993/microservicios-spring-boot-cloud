package com.paymentchain.transaction.controller;

import com.paymentchain.transaction.entities.Transaction;
import com.paymentchain.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    TransactionRepository transactionRepository;

    @Autowired
    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping
    public List<Transaction> findAll() {
        return  this.transactionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> findById(@PathVariable long id) {
        Optional<Transaction> transaction = this.transactionRepository.findById(id);
        if(transaction.isPresent()){
            return ResponseEntity.ok(transaction.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Transaction>  save(@RequestBody Transaction transaction) {
        Transaction newTransaction = this.transactionRepository.save(transaction);
        return ResponseEntity.ok(newTransaction);
    }

    @GetMapping("/customer/transactions")
    public List<Transaction> get(@RequestParam String ibanAccount) {
        return this.transactionRepository.findByIbanAccount(ibanAccount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction>  update(@PathVariable long id,@RequestBody Transaction transaction) {
        Transaction find = transactionRepository.findById(id).get();
        if (find != null) {
            find.setAmount(transaction.getAmount());
            find.setChannel(transaction.getChannel());
            find.setDate(transaction.getDate());
            find.setDescription(transaction.getDescription());
            find.setFee(transaction.getFee());
            find.setIbanAccount(transaction.getIbanAccount());
            find.setReference(transaction.getReference());
            find.setStatus(transaction.getStatus());
        }
        Transaction save = transactionRepository.save(find);
        return ResponseEntity.ok(save);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Optional<Transaction> transaction = this.transactionRepository.findById(id);
        if(transaction.isPresent()) {
            this.transactionRepository.delete(transaction.get());
        }
        return ResponseEntity.ok().build();
    }

}
