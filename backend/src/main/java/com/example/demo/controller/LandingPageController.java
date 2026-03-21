package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.LandingPageInfo;

@RestController
public class LandingPageController {

    @GetMapping("/landing")
    public LandingPageInfo getLandingPage() {
        return new LandingPageInfo(
                "Reflect Me",
                "Welcome to Self Reflection App",
                "Make yourself feel better fr fr.",
                List.of(
                        "Create account",             
                        "Track your progress"
                ),
                "Sign In",
                "Sign Up"
        );
    }
}