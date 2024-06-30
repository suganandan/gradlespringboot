package com.sugan.gradlespringboot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(HelloController.class)
class HelloControllerAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetWelcome() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().is(200))
                .andExpect(content().string("Welcome to Gradle Spring boot"));
    }
}