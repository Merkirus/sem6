package com.in28minutes.springboot.microservice.example.forex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/support")
@RestController
public class ForexController {
    private final IForexService forexService;

    public ForexController(IForexService forexService) {
        this.forexService = forexService;
    }

    @PostMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestBody String text) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(forexService.encrypt(text));
    }

    @PostMapping("/decrypt")
    public ResponseEntity<String> decrypt(@RequestBody String text) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(forexService.decrypt(text));
    }
}