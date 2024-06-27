package com.in28minutes.springboot.microservice.example.forex;

import org.springframework.stereotype.Service;

@Service
public class ForexService implements IForexService {
    @Override
    public String encrypt(String text) {
        try {
            return EncryptionUtil.encrypt(text);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String decrypt(String text) {
        try {
            return EncryptionUtil.decrypt(text);
        } catch (Exception e) {
            return null;
        }
    }
}
