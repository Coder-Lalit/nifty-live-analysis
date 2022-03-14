package com.wealth.stock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ping {
    @GetMapping("/ping")
    public String getStatus(){
        return "I'm Nifty app and Up";
    }
}
