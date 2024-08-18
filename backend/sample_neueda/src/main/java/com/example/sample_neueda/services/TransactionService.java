package com.example.sample_neueda.services;

import com.example.sample_neueda.entities.accounts;
import com.example.sample_neueda.entities.stocks;
import com.example.sample_neueda.entities.transaction;
import com.example.sample_neueda.repo.TransactionRepository;
import com.example.sample_neueda.repo.accountsRepo;
import com.example.sample_neueda.repo.stocksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionService {

    private stocksRepo stockRepository;
    private TransactionRepository transactionRepository;
    private RestTemplate restTemplate;
    private StockApiService stockService;
    private accountsRepo accountsRepository;

    @Autowired
    public TransactionService(stocksRepo stockRepository, TransactionRepository transactionRepository, RestTemplate restTemplate, StockApiService stockService, accountsRepo accountsRepository) {
        this.stockRepository = stockRepository;
        this.transactionRepository = transactionRepository;
        this.restTemplate = restTemplate;
        this.stockService = stockService;
        this.accountsRepository = accountsRepository;
    }

    public List<transaction> getTransactionsByStock(String symbol) {
        stocks stock = stockRepository.findBySymbol(symbol);
        return stock != null ? transactionRepository.findByStock(stock) : Collections.emptyList();
    }

    public void buyStock(String symbol, int quantity,String number) {
        accounts account=accountsRepository.findByNumber(number);
        stocks stock = stockRepository.findBySymbol(symbol);
        double price;
        if (stock == null) {
//            String API_KEY = "3TI09ONCI94L60D8";
//            String BASE_URL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s";
//            String url = String.format(BASE_URL, symbol, API_KEY);
//            StockApiResponse response = restTemplate.getForObject(url, StockApiResponse.class);
//            if (response != null && response.getGlobalQuote() != null) {
//                price= response.getGlobalQuote().getPrice();}
            stock=stockService.fetchStockData(symbol);
        }

        price=stock.getCurrentPrice();
        double currentBalance=account.getBalance();
        double buyAmount=price*quantity;
        if (!Objects.equals(account.getType(), "SAVINGS")){
            throw new IllegalArgumentException("This is not a savings account");
        }
        if(currentBalance<buyAmount){
            throw new IllegalArgumentException("You do not have enough money in this account to make the purchase");
        }
        transaction transaction = new transaction("BUY", quantity, price, LocalDateTime.now(), stock,account);
        transactionRepository.save(transaction);
        account.setBalance(currentBalance-buyAmount);
        accountsRepository.save(account);
    }

    public void sellStock(String symbol, int quantity,String number) {
        accounts account=accountsRepository.findByNumber(number);
        stocks stock = stockRepository.findBySymbol(symbol);
        if (stock == null) {
            throw new IllegalArgumentException("You do not own this stock");
        }
        int owned_quantity=ownedQuantityInduvidual(symbol) ;
        if (owned_quantity<quantity) {
            throw new IllegalArgumentException("You do not have enough of this stock");
        }
        if (!Objects.equals(account.getType(), "SAVINGS")){
            throw new IllegalArgumentException("This is not a savings account");
        }
        double price=stock.getCurrentPrice();
        double currentBalance=account.getBalance();
        double sellAmount=price*quantity;
        transaction transaction = new transaction("SELL", quantity, price, LocalDateTime.now(), stock,account);
        transactionRepository.save(transaction);
        account.setBalance(currentBalance+sellAmount);
        accountsRepository.save(account);
//        if (owned_quantity==quantity) {
//            stockRepository.delete(stock);
//        }
    }

    public int ownedQuantityInduvidual(String symbol) {
        stocks stock = stockRepository.findBySymbol(symbol);
        if (stock == null) {
            throw new IllegalArgumentException("Stock with symbol " + symbol + " not found");
        }
        List<transaction> transactions = transactionRepository.findByStock(stock);
        int totalQuantity = 0;
        for (transaction transaction : transactions) {
            if ("BUY".equalsIgnoreCase(transaction.getType())) {
                totalQuantity += transaction.getQuantity();
            } else if ("SELL".equalsIgnoreCase(transaction.getType())) {
                totalQuantity -= transaction.getQuantity();
            }
        }
        return totalQuantity;
    }

    public double amountInvestedInduvidual(String symbol) {
        stocks stock = stockRepository.findBySymbol(symbol);
        if (stock == null) {
            throw new IllegalArgumentException("Stock with symbol " + symbol + " not found");
        }
        List<transaction> transactions = transactionRepository.findByStock(stock);
        transactions.sort(Comparator.comparing(transaction::getTimestamp));
        double amount = 0;
        Queue<transaction> buyQueue = new LinkedList<>();
        for (transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("BUY")) {
                buyQueue.add(transaction);
            } else if (transaction.getType().equalsIgnoreCase("SELL")) {
                int sellQuantity = transaction.getQuantity();

                while (sellQuantity > 0 && !buyQueue.isEmpty()) {
                    transaction buyTransaction = buyQueue.peek();
                    int buyQuantity = buyTransaction.getQuantity();

                    if (sellQuantity >= buyQuantity) {
                        sellQuantity -= buyQuantity;
                        buyQueue.poll();
                    } else {
                        buyTransaction.setQuantity(buyQuantity - sellQuantity);
                        sellQuantity = 0;
                    }
                }
            }
        }

        for (transaction buyTransaction : buyQueue) {
            amount += buyTransaction.getQuantity() * buyTransaction.getPrice();
        }

        return amount;
    }

    public double currentValueInduvidual(String symbol) {
        stocks stock = stockRepository.findBySymbol(symbol);
        if (stock == null) {
            throw new IllegalArgumentException("You do not own this stock");
        }
        int ownedQuantity = ownedQuantityInduvidual(symbol);
        double currentPrice = stock.getCurrentPrice();
        return ownedQuantity * currentPrice;
    }

    public double GainOrLossInduvidual(String symbol) {
        stocks stock = stockRepository.findBySymbol(symbol);
        if (stock == null) {
            throw new IllegalArgumentException("Stock with symbol " + symbol + " not found");
        }

        List<transaction> transactions = transactionRepository.findByStock(stock);
        transactions.sort(Comparator.comparing(transaction::getTimestamp));

        double totalGainOrLoss = 0;
        Queue<transaction> buyQueue = new LinkedList<>();

        for (transaction transaction : transactions) {
            if ("BUY".equalsIgnoreCase(transaction.getType())) {
                buyQueue.add(transaction);
            } else if ("SELL".equalsIgnoreCase(transaction.getType())) {
                int sellQuantity = transaction.getQuantity();
                double sellPrice = transaction.getPrice();

                while (sellQuantity > 0 && !buyQueue.isEmpty()) {
                    transaction buyTransaction = buyQueue.peek();
                    int buyQuantity = buyTransaction.getQuantity();
                    double buyPrice = buyTransaction.getPrice();

                    if (buyQuantity > sellQuantity) {
                        totalGainOrLoss += (sellPrice - buyPrice) * sellQuantity;
                        sellQuantity = 0;
                    } else {
                        totalGainOrLoss += (sellPrice - buyPrice) * buyQuantity;
                        sellQuantity -= buyQuantity;
                        buyQueue.poll();
                    }
                }
            }
        }

        return totalGainOrLoss;
    }

    public double GainOrLossTotal() {
        List<stocks> allStocks = stockRepository.findAll();
        double totalGainOrLoss = 0;

        for (stocks stock : allStocks) {
            totalGainOrLoss += GainOrLossInduvidual(stock.getSymbol());
        }

        return totalGainOrLoss;
    }

    public double amountInvestedTotal() {
        List<stocks> allStocks = stockRepository.findAll();
        double total = 0;

        for (stocks stock : allStocks) {
            total += amountInvestedInduvidual(stock.getSymbol());
        }

        return total;
    }

    public double currentValueTotal() {
        List<stocks> allStocks = stockRepository.findAll();
        double total = 0;

        for (stocks stock : allStocks) {
            total += currentValueInduvidual(stock.getSymbol());
        }

        return total;
    }

    public TimeSeriesDailyResponse fetchTimeSeriesData(String symbol) {
//        String API_KEY = "3TI09ONCI94L60D8";
//        String BASE_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s";
        String BASE_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=demo";
//        String url = String.format(BASE_URL, symbol, API_KEY);
        String url = String.format(BASE_URL, symbol);
        return restTemplate.getForObject(url, TimeSeriesDailyResponse.class);
    }

    public Map<String, Object> findTopGainerAndLoser() {
        List<stocks> allStocks = stockRepository.findAll();
        Map<String, Double> performanceMap = new HashMap<>();

        for (stocks stock : allStocks) {
            int qty = ownedQuantityInduvidual(stock.getSymbol());
            if (qty > 0) {
                List<transaction> transactions = transactionRepository.findByStock(stock);
                transactions.sort(Comparator.comparing(transaction::getTimestamp));

                if (transactions.size() >= 2) {
                    double earliestPrice = transactions.get(0).getPrice();
                    double latestPrice = transactions.get(transactions.size() - 1).getPrice();

                    double percentageChange = ((latestPrice - earliestPrice) / earliestPrice) * 100;
                    performanceMap.put(stock.getSymbol(), percentageChange);
                }
            }
        }

        if (performanceMap.isEmpty()) {
            throw new IllegalStateException("No stocks with sufficient data for Gainer and Loser calculations.");
        }

        Map<String, Object> result = new HashMap<>();
        Map.Entry<String, Double> topGainer = Collections.max(performanceMap.entrySet(), Map.Entry.comparingByValue());
        Map.Entry<String, Double> topLoser = Collections.min(performanceMap.entrySet(), Map.Entry.comparingByValue());

        result.put("topGainer", Map.of("symbol", topGainer.getKey(), "percentageChange", topGainer.getValue()));
        result.put("topLoser", Map.of("symbol", topLoser.getKey(), "percentageChange", topLoser.getValue()));

        return result;
    }

}
