package com.example.sample_neueda.control;

import com.example.sample_neueda.dto.TransactionRequest;
import com.example.sample_neueda.entities.accounts;
import com.example.sample_neueda.entities.stocks;
import com.example.sample_neueda.entities.transaction;
import com.example.sample_neueda.services.AccountsService;
import com.example.sample_neueda.services.StockApiService;
import com.example.sample_neueda.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.*;
@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private StockApiService stockService;
    private TransactionService transactionService;
    private AccountsService accountsService;

    @Autowired
    public StockController(StockApiService stockService, TransactionService transactionService,AccountsService accountsService) {
        this.stockService = stockService;
        this.transactionService = transactionService;
        this.accountsService = accountsService;
    }

    // Fetch stock data from external API
    @GetMapping("/fetch/{symbol}")
    public ResponseEntity<stocks> fetchStock(@PathVariable String symbol) {
        stocks stock = stockService.viewExternalData(symbol);
        if (stock != null) {
            return ResponseEntity.ok(stock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get stock by symbol from the database
    @GetMapping("/{symbol}")
    public ResponseEntity<stocks> getStock(@PathVariable String symbol) {
        stocks stock = stockService.getStockBySymbol(symbol);
        if (stock != null) {
            return ResponseEntity.ok(stock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Buy stock
    @PostMapping("/buy")
    public ResponseEntity<String> buyStock(@RequestParam String symbol, @RequestParam int quantity,@RequestParam String number) {
        try {
            transactionService.buyStock(symbol, quantity,number);
            return ResponseEntity.ok("Stock purchased successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Sell stock
    @PostMapping("/sell")
    public ResponseEntity<String> sellStock(@RequestParam String symbol, @RequestParam int quantity,@RequestParam String number) {
        try {
            transactionService.sellStock(symbol, quantity,number);
            return ResponseEntity.ok("Stock sold successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // add account
    @PostMapping("/addAccount")
    public ResponseEntity<String> addAccount(@RequestParam String name,@RequestParam String number,@RequestParam String type, @RequestParam double balance,@RequestParam double interest ) {
        try {
            accountsService.addAccount(name, number,type,balance,interest);
            return ResponseEntity.ok("Account added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // display all accounts
    @GetMapping("/accounts")
    public ResponseEntity <List<accounts>> calculateQuantityInduvidual() {
        try {
            List<accounts> allAccounts= accountsService.displayAccounts();
            return ResponseEntity.ok(allAccounts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // quantity of stock
    @GetMapping("/quantity/{symbol}")
    public ResponseEntity<Integer> calculateQuantityInduvidual(@PathVariable String symbol) {
        try {
            int quantity = transactionService.ownedQuantityInduvidual(symbol);
            return ResponseEntity.ok(quantity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    // amount invested in a stock
    @GetMapping("/amount/{symbol}")
    public ResponseEntity<Double> amountInvestedInduvidual(@PathVariable String symbol) {
        try {
            double amount = transactionService.amountInvestedInduvidual(symbol);
            return ResponseEntity.ok(amount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    // current value of stock
    @GetMapping("/value/{symbol}")
    public ResponseEntity<Double> currentValueInduvidual(@PathVariable String symbol) {
        try {
            double amount = transactionService.currentValueInduvidual(symbol);
            return ResponseEntity.ok(amount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    // Calculate profit for a stock
    @GetMapping("/profit/{symbol}")
    public ResponseEntity<Double> calculateProfit(@PathVariable String symbol) {
        try {
            double profit = transactionService.GainOrLossInduvidual(symbol);
            return ResponseEntity.ok(profit);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //total amount invested in all stocks
    @GetMapping("/totalInvested")
    public ResponseEntity<Double> amountInvestedTotal() {
        try {
            double amount = transactionService.amountInvestedTotal();
            return ResponseEntity.ok(amount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    // total value of all stocks
    @GetMapping("/valueTotal")
    public ResponseEntity<Double> currentValueTotal() {
        try {
            double amount = transactionService.currentValueTotal();
            return ResponseEntity.ok(amount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //total profit or loss
    @GetMapping("/profit")
    public ResponseEntity<Double> calculateProfitTotal() {
        try {
            double profit = transactionService.GainOrLossTotal();
            return ResponseEntity.ok(profit);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get all transactions for a stock
    @GetMapping("/transactions/{symbol}")
    public ResponseEntity<List<transaction>> getTransactions(@PathVariable String symbol) {
        stocks stock = stockService.getStockBySymbol(symbol);
        if (stock != null) {
            List<transaction> transactions = transactionService.getTransactionsByStock(symbol);
            return ResponseEntity.ok(transactions);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/transaction")
    public ResponseEntity<String> postTransaction(@RequestBody TransactionRequest request) {
        try {
            if ("BUY".equalsIgnoreCase(request.getType())) {
                transactionService.buyStock(request.getSymbol(), request.getQuantity(), request.getNumber());
            } else if ("SELL".equalsIgnoreCase(request.getType())) {
                transactionService.sellStock(request.getSymbol(), request.getQuantity(), request.getNumber());
            } else {
                throw new IllegalArgumentException("Invalid transaction type.");
            }
            return ResponseEntity.ok("Transaction processed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //get transactions for all stocks
    @GetMapping("/transactions")
    public ResponseEntity<List<transaction>> getTransactionsAll() {
        List<stocks> allStocks = stockService.getAllStocks();
        List<transaction> allTransactions = new ArrayList<>();
        for (stocks stock : allStocks) {
            List<transaction> transactions = transactionService.getTransactionsByStock(stock.getSymbol());
            allTransactions.addAll(transactions);
        }

        return ResponseEntity.ok(allTransactions);
    }

    // get all stocks in stocks table ( stocks currently owned
    @GetMapping("/allStocks")
    public ResponseEntity<List<stocks>> getStocksAll() {
        List<stocks> allStocks = stockService.getAllStocks();
        return ResponseEntity.ok(allStocks);
    }

    // returns weekly top gainer and loser ( gainer first, loser sceond)
    @GetMapping("/gainerloser")
    public ResponseEntity<?> findTopGainerAndLoser() {
        try {
            Map<String, Object> result = transactionService.findTopGainerAndLoser();
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No stocks available for Gainer and Loser calculations.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    // recommendations
    @GetMapping("/recommendations")
    public ResponseEntity<Map<String, List<Map<String, String>>>> getStockRecommendations() {
        try {
            // Fetch data from the API
            Map<String, List<Map<String, String>>> stockData = stockService.fetchStockDataFromAPI();

            // Return the map wrapped in a ResponseEntity
            return ResponseEntity.ok(stockData);
        } catch (Exception e) {
            e.printStackTrace();
            // Return an empty map with a 500 status code in case of error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<>());
        }
    }


}
