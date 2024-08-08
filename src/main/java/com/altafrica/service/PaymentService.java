package com.altafrica.service;


import com.altafrica.exception.ResourceNotFoundException;
import com.altafrica.model.Transaction;
import com.altafrica.repository.TransactionRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PaymentService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Value("${paystack.secret.key}")
    private String paystackSecretKey;

    private static final String PAYSTACK_INITIATE_URL = "https://api.paystack.co/transaction/initialize";
    private static final String PAYSTACK_VERIFY_URL = "https://api.paystack.co/transaction/verify/";

    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());

    public Transaction initiatePayment(String email, String domain, int amount, String callbackUrl) {
        Transaction transaction = new Transaction();
        transaction.setEmail(email);
        transaction.setDomain(domain);
        transaction.setReference(UUID.randomUUID().toString());
        transaction.setStatus("pending");
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        logger.info("Transaction saved with reference: " + transaction.getReference());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(paystackSecretKey);

        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("amount", amount);
        body.put("reference", transaction.getReference());
        body.put("callback_url", callbackUrl);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(PAYSTACK_INITIATE_URL, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject responseBody = new JSONObject(response.getBody());
            String accessCode = responseBody.getJSONObject("data").getString("access_code");
            transaction.setStatus(accessCode);
            transactionRepository.save(transaction);
            logger.info("Transaction updated with access code: " + accessCode);
        } else {
            logger.log(Level.SEVERE, "Failed to initiate transaction: " + response.getStatusCode());
        }

        return transaction;
    }

    public Transaction verifyPayment(String reference) {
        Transaction transaction = transactionRepository.findByReference(reference);
        if (transaction == null) {
            logger.log(Level.WARNING, "Transaction not found for reference: " + reference);
            throw new ResourceNotFoundException("Transaction not found");
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(paystackSecretKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(PAYSTACK_VERIFY_URL + reference, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject responseBody = new JSONObject(response.getBody());
            String status = responseBody.getJSONObject("data").getString("status");
            if ("success".equals(status)) {
                transaction.setStatus("success");
                transactionRepository.save(transaction);
                logger.info("Transaction verified successfully: " + reference);
            } else {
                transaction.setStatus("failed");
                transactionRepository.save(transaction);
                logger.log(Level.WARNING, "Transaction verification failed: " + reference);
            }
        } else {
            logger.log(Level.SEVERE, "Failed to verify transaction: " + response.getStatusCode());
        }

        return transaction;
    }
}
