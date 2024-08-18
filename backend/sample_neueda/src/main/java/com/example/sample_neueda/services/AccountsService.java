package com.example.sample_neueda.services;

import com.example.sample_neueda.entities.accounts;
import com.example.sample_neueda.repo.TransactionRepository;
import com.example.sample_neueda.repo.accountsRepo;
import com.example.sample_neueda.repo.stocksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountsService {
    private accountsRepo accountsRepository;

    @Autowired
    public AccountsService(accountsRepo accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    // apply daily interest
    public void applyDailyInterest(String number) {
        accounts account=accountsRepository.findByNumber(number);
        if (account.getType().equalsIgnoreCase("SAVINGS")) {
        if (account.getLastInterestApplied()!= null) {
            long daysSinceLastApplication = java.time.Duration.between(account.getLastInterestApplied().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
            if (daysSinceLastApplication > 0) {
                double dailyRate = account.getInterest() / 365 / 100;
                double balance = (Math.pow(1 + dailyRate, daysSinceLastApplication))* account.getBalance();
                account.setBalance(balance);
                account.setLastInterestApplied(LocalDate.now());
                accountsRepository.save(account);
            }
        }}
    }

    // apply interest on all savings accounts
    public void applyInterestToAllAccounts() {
        List<accounts> allAccounts = accountsRepository.findAll();
        for (accounts account : allAccounts) {
            applyDailyInterest(account.getNumber());
             // Save the updated account
        }
    }

    public void addAccount(String name, String number,String type, double balance, double interest){
        accounts account = new accounts(name, number,type,balance,interest, LocalDate.now());
        accountsRepository.save(account);
    }

    public List<accounts> displayAccounts(){
        List<accounts> allAccounts = accountsRepository.findAll();
        return allAccounts;
    }

}
