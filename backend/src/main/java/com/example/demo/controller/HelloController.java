package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.Quote;
import com.example.demo.repository.QuoteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HelloController {

    private final QuoteRepository quoteRepository;

    public HelloController(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Backend is working";
    }

    @GetMapping("/message")
    public Message getMessage() {
        return new Message("Hello from backend");
    }

    // these are just tests for the database connection
    // IGNORE THEM
    @PostMapping("/test-quote")
    public Quote addTestQuote(@RequestBody Quote quote) {
        return quoteRepository.save(quote);
    }

    @GetMapping("/test-quotes")
    public List<Quote> getTestQuotes() {
        return quoteRepository.findAll();
    }
}