package com.sugan.gradlespringboot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
@GetMapping("/")
    public ResponseEntity<String> getWelcome(){
         return ResponseEntity.status(200).body("Welcome to Gradle Spring boot");
    }

    public int addNumbers(int val1, int val2){
    return val1+val2;
    }
}
