package com.stock.stockpriceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockPriceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockPriceServiceApplication.class, args);
    }
}