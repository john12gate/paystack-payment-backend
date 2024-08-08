package com.altafrica.controller;


import com.altafrica.dto.PaymentRequest;
import com.altafrica.model.Transaction;
import com.altafrica.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payment Controller", description = "API for managing payments with Paystack.")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Initiate a payment", description = "Initiate a new payment transaction with Paystack.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction initiated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment request data"),
            @ApiResponse(responseCode = "500", description = "Error initiating transaction")
    })
    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Transaction> initiatePayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        int amount = paymentRequest.getAmount(); // amount in kobo
        String callbackUrl = "http://localhost:9090/api/v1/payments/callback"; // Set your callback URL
        Transaction transaction = paymentService.initiatePayment(paymentRequest.getEmail(), paymentRequest.getDomain(), amount, callbackUrl);
        return ResponseEntity.ok(transaction);
    }

    @Operation(summary = "Verify a payment", description = "Verify the status of a payment transaction with Paystack.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction verified successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "Error verifying transaction")
    })
    @GetMapping("/payment-status")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Transaction> verifyPayment(@RequestParam String reference) {
        Transaction transaction = paymentService.verifyPayment(reference);
        return ResponseEntity.ok(transaction);
    }

    @Operation(summary = "Payment callback", description = "Handle Paystack payment callback.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Callback handled successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid callback data"),
            @ApiResponse(responseCode = "500", description = "Error handling callback")
    })
    @PostMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestBody Map<String, Object> payload) {
        String reference = (String) payload.get("reference");
        if (reference == null) {
            return ResponseEntity.badRequest().body("Reference not found in callback data");
        }
        Transaction transaction = paymentService.verifyPayment(reference);
        return ResponseEntity.ok("Callback handled successfully. Transaction status: " + transaction.getStatus());
    }
}
