package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeditationController {
    @GetMapping("/get")
    public String GetQuote(){
        return "Test";
    }
}
