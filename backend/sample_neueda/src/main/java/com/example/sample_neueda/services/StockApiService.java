package com.example.sample_neueda.services;

import com.example.sample_neueda.entities.stocks;
import com.example.sample_neueda.entities.transaction;
import com.example.sample_neueda.repo.stocksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

import java.util.stream.Collectors;

import java.util.List;
import java.util.*;

@Service
public class StockApiService {

    private final RestTemplate restTemplate;
    private stocksRepo stockRepository;

    @Autowired
    public StockApiService(RestTemplate restTemplate, stocksRepo stockRepository) {
        this.restTemplate = restTemplate;
        this.stockRepository = stockRepository;
    }

    // get all stocks in stock table
    public List<stocks> getAllStocks() {
        List<stocks> allStocks = stockRepository.findAll();
        return allStocks;
    }

    // retrieve external dat for viewing purpose only ( does not update stocks table)
    public stocks viewExternalData(String symbol) {
        String API_KEY = "3TI09ONCI94L60D8";
        String BASE_URL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s";
        String url = String.format(BASE_URL, symbol, API_KEY);
        StockApiResponse response = restTemplate.getForObject(url, StockApiResponse.class);

        if (response != null && response.getGlobalQuote() != null) {
            if (response.getGlobalQuote().getSymbol() == null) {
                // Handle error scenario
                throw new IllegalArgumentException("Stock Not Found");
            }
            stocks stock = new stocks();
            stock.setSymbol(response.getGlobalQuote().getSymbol());
            stock.setName(response.getGlobalQuote().getName());
            stock.setCurrentPrice(response.getGlobalQuote().getPrice());

            return stock;
        } else {
            throw new IllegalArgumentException("Stock Not Found");
        }
    }

    // retrieve external data ( used only for buy as it update stocks table)
    public stocks fetchStockData(String symbol) {
        String API_KEY = "3TI09ONCI94L60D8";
        String BASE_URL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s";
        String url = String.format(BASE_URL, symbol, API_KEY);
        StockApiResponse response = restTemplate.getForObject(url, StockApiResponse.class);

        if (response != null && response.getGlobalQuote() != null) {
            if (response.getGlobalQuote().getSymbol() == null) {
                // Handle error scenario
                throw new IllegalArgumentException("Stock Not Found");
            }
            stocks stock = new stocks();
            stock.setSymbol(response.getGlobalQuote().getSymbol());
            stock.setName(response.getGlobalQuote().getName());
            stock.setCurrentPrice(response.getGlobalQuote().getPrice());


            // Save the stock to the database
            stockRepository.save(stock);
            return stock;
        } else {
            throw new IllegalArgumentException("Stock Not Found");
        }
    }

    //update current price of a particular stock in stock table
    public void updatePriceInduvidual(String symbol) {
        String API_KEY = "3TI09ONCI94L60D8";
        String BASE_URL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s";
        String url = String.format(BASE_URL, symbol, API_KEY);
        StockApiResponse response = restTemplate.getForObject(url, StockApiResponse.class);

        if (response != null && response.getGlobalQuote() != null) {
            stocks stock = stockRepository.findBySymbol(symbol);
            stock.setCurrentPrice(response.getGlobalQuote().getPrice());
            stockRepository.save(stock);
        }
    }

    //update price of all stocks
    public void updatePrice() {
        List<stocks> allStocks = stockRepository.findAll();

        for (stocks stock : allStocks) {
            updatePriceInduvidual(stock.getSymbol());
        }
    }


    // get stock from our table
    public stocks getStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }


    // recommendations that are not already owned
    public List<String> getTop10PerformingStocks() {
//        String API_KEY = "ZXF2MVBTERM9DYRX";
//        String BASE_URL = "https://www.alphavantage.co/query?function=TOP_GAINERS_LOSERS&apikey=%s";
//      String url = String.format(BASE_URL, API_KEY);
        String url = "https://www.alphavantage.co/query?function=TOP_GAINERS_LOSERS&apikey=demo";
        System.out.println("Hi URL : " + url);
        // Fetch data from the Alpha Vantage API
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String jsonResponse = response.getBody();

        if (jsonResponse == null) {
            return new ArrayList<>();
        }
        List<stocks> allStocks = stockRepository.findAll();
        List<String> ownedSymbols = new ArrayList<>();
        for (stocks stock : allStocks) {
            ownedSymbols.add(stock.getSymbol());
        }

        List<String> top10Symbols = new ArrayList<>();
        try {
            // Parse the JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            JsonNode topGainersNode = rootNode.path("top_gainers");

            // Iterate through the top gainers
            for (JsonNode stockNode : topGainersNode) {
                if (top10Symbols.size() >= 10) {
                    break;
                }
                String ticker = stockNode.path("ticker").asText();
                top10Symbols.add(ticker);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Remove owned symbols from the top 10 symbols
        top10Symbols = top10Symbols.stream()
                .filter(symbol -> !ownedSymbols.contains(symbol))
                .collect(Collectors.toList());

        return top10Symbols;
    }


    public Map<String, List<Map<String, String>>> fetchStockDataFromAPI() {
        // Replace with your actual API key
        String API_KEY = "YOUR_ACTUAL_API_KEY";
        String BASE_URL = "https://www.alphavantage.co/query?function=TOP_GAINERS_LOSERS&apikey=%s";
//        String url = String.format(BASE_URL, API_KEY);
        String url = "https://www.alphavantage.co/query?function=TOP_GAINERS_LOSERS&apikey=demo";

        Map<String, List<Map<String, String>>> stockData = new HashMap<>();

        try {
            // Fetch data from the Alpha Vantage API
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String jsonResponse = response.getBody();

            if (jsonResponse == null || jsonResponse.isEmpty()) {
                return new HashMap<>(); // Return empty map if no response or empty response
            }

            // Parse the JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);

            // Extract the different categories
            stockData.put("top_gainers", parseStockList(rootNode.path("top_gainers")));
            stockData.put("top_losers", parseStockList(rootNode.path("top_losers")));
            stockData.put("most_actively_traded", parseStockList(rootNode.path("most_actively_traded")));

        } catch (Exception e) {
            e.printStackTrace(); // Log any errors
        }

        return stockData;
    }

    private List<Map<String, String>> parseStockList(JsonNode stockListNode) {
        List<Map<String, String>> stocks = new ArrayList<>();

        for (JsonNode stockNode : stockListNode) {
            Map<String, String> stock = new HashMap<>();
            stock.put("ticker", stockNode.path("ticker").asText());
            stock.put("price", stockNode.path("price").asText());
            stock.put("change_amount", stockNode.path("change_amount").asText());
            stock.put("change_percentage", stockNode.path("change_percentage").asText());
            stock.put("volume", stockNode.path("volume").asText());
            stocks.add(stock);
        }

        return stocks;
    }

}

