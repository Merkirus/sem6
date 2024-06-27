package com.in28minutes.springboot.microservice.example.forex;

public interface IForexService {
    public abstract String encrypt(String text);
    public abstract String decrypt(String text);
}
