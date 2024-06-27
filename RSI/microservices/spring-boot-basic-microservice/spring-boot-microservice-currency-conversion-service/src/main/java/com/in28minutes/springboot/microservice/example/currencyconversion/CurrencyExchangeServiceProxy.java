package com.in28minutes.springboot.microservice.example.currencyconversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "forex-service")
public interface CurrencyExchangeServiceProxy {
    @PostMapping("/support/encrypt")
    public abstract ResponseEntity<String> encrypt(@RequestBody String text);
    @PostMapping("/support/decrypt")
    public abstract ResponseEntity<String> decrypt(@RequestBody String text);
}