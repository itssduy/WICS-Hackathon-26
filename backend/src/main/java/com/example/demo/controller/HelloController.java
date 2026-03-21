package com.example.demo.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Message;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Backend is working";
    }
    @GetMapping("/message")
    public Message getMessage() {
    return new Message("Hello from backend");
    }
}