package com.wealth.stock.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.restassured.RestAssured.given;

@RestController
public class ping {
    @GetMapping("/ping")
    public String getStatus(){
        return "I'm Nifty app and Up";
    }

    @Scheduled(fixedDelay = 1200000)
    public void checkStatus(){
        System.out.println(
                given().when().get("https://nifty-live-analysis.herokuapp.com/ping").then().extract().asString()
        );
    }
}
