package com.example.sample_neueda.schedule;

import com.example.sample_neueda.services.AccountsService;
import com.example.sample_neueda.services.StockApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private AccountsService accountService;
    private StockApiService stockApiService;

    @Autowired
    public ScheduledTasks(AccountsService accountService,StockApiService stockApiService) {
        this.stockApiService = stockApiService;
        this.accountService = accountService;

    }

    @Scheduled(cron = "0 0 0 * * *") // daily scheduled
    public void applyInterest() {
        accountService.applyInterestToAllAccounts();
    }

    @Scheduled(cron = "0 0 0 * * *") // daily scheduled
    public void updatePrice() {
        stockApiService.updatePrice();
    }


}

