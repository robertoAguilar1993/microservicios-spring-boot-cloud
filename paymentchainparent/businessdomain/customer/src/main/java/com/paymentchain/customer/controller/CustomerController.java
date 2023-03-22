package com.paymentchain.customer.controller;

import com.paymentchain.customer.business.transactions.BussinesTransaction;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.exception.BussinesRuleException;
import com.paymentchain.customer.repository.CustomerRepository;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("customer")
public class CustomerController {

    CustomerRepository customerRepository;
    BussinesTransaction bussinesTransaction;

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public CustomerController(CustomerRepository customerRepository, WebClient.Builder webClientBuilder, BussinesTransaction bussinesTransaction) {
        this.customerRepository = customerRepository;
        this.webClientBuilder = webClientBuilder;
        this.bussinesTransaction = bussinesTransaction;
    }

    @Value("${user.role}")
    private String role;

    //webClient requires HttpClient library to work propertly
    HttpClient client = HttpClient.create()
            //Connection Timeout: is a period within which a connection between a client and a server must be established
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            //Response Timeout: The maximun time we wait to receive a response after sending a request
            .responseTimeout(Duration.ofSeconds(1))
            // Read and Write Timeout: A read timeout occurs when no data was read within a certain
            //period of time, while the write timeout when a write operation cannot finish at a specific time
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

    @GetMapping
    public ResponseEntity<List<Customer>> findAll() {
        System.out.print("el role es : " +role);
        List<Customer> customers = this.customerRepository.findAll();
        if(CollectionUtils.isEmpty(customers)){
            return ResponseEntity.noContent().build();
        }
        return  ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable long id) {
        return this.customerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customer>  save(@RequestBody Customer customer) throws UnknownHostException, BussinesRuleException {
        Customer newCustomer = this.bussinesTransaction.save(customer);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer>  update(@PathVariable long id,  @RequestBody Customer customer) {
        Customer newCustomer = this.customerRepository.save(customer);
        return ResponseEntity.ok(newCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Optional<Customer> customer = this.customerRepository.findById(id);
        if(customer.isPresent()) {
            this.customerRepository.delete(customer.get());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/full")
    public Customer getCustomerByCode(@RequestParam String code) {
        Customer customer = this.bussinesTransaction.get(code);
        return customer;
    }

}
