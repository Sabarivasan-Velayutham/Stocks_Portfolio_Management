package com.example.sample_neueda.schedule;
import com.example.sample_neueda.services.StockApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class Startuptasks {

    private StockApiService stockApiService;

    @Autowired
    public Startuptasks( StockApiService stockApiService) {
        this.stockApiService = stockApiService;
    }

    @PostConstruct
    public void init() {
        // Code to execute once after application startup
        stockApiService.updatePrice();
    }

}