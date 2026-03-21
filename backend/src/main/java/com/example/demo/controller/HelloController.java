package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.Question;
import com.example.demo.repository.QuestionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HelloController {

    private final QuestionRepository questionRepository;

    public HelloController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
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
    @PostMapping("/questions")
    public Question addQuestion(@RequestBody Question question) {
        return questionRepository.save(question);
    }

    @GetMapping("/questions")
    public List<Question> getQuestions() {
        return questionRepository.findAll();
    }
}