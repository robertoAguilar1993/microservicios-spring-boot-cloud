package com.paymentchain.billing.controller;

import com.paymentchain.billing.common.InvoiceRequestMapper;
import com.paymentchain.billing.common.InvoiceResponseMapper;
import com.paymentchain.billing.dto.InvoiceRequest;
import com.paymentchain.billing.dto.InvoiceResponse;
import com.paymentchain.billing.entities.Invoice;
import com.paymentchain.billing.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Billing API", description = "This APi serve all functionality for management Invoices")
@RestController
@RequestMapping("/billing")
public class InvoiceController {
    @Autowired
    InvoiceRepository customerRepository;

    @Autowired
    InvoiceRequestMapper irm;

    @Autowired
    InvoiceResponseMapper irspm;

    @Operation(description = "Return all invoices bundled into Response", summary ="Return 204 if no data found")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",description = "Exito"),
            @ApiResponse(responseCode = "500", description = "Internal error")})
    @GetMapping()
    public List<InvoiceResponse> list() {
        List<Invoice> findAll = customerRepository.findAll();
        List<InvoiceResponse> InvoiceListToInvoiceResposeList = irspm.InvoiceListToInvoiceResposeList(findAll);
        return InvoiceListToInvoiceResposeList;
    }

    @GetMapping("/{id}")
    public Invoice get(@PathVariable String id) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody Invoice input) {
        return null;
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody InvoiceRequest input) {
        Invoice InvoiceRequestToInvoice = irm.InvoiceRequestToInvoice(input);
        Invoice save = customerRepository.save(InvoiceRequestToInvoice);
        InvoiceResponse InvoiceToInvoiceRespose = irspm.InvoiceToInvoiceRespose(save);
        return ResponseEntity.ok(InvoiceToInvoiceRespose);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return null;
    }
}
