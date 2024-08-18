package com.example.sample_neueda.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class TimeSeriesDailyResponse {
    @JsonProperty("Time Series (Daily)")
    private Map<String, DailyStockData> timeSeries;

    public Map<String, DailyStockData> getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(Map<String, DailyStockData> timeSeries) {
        this.timeSeries = timeSeries;
    }


public static class DailyStockData {
    @JsonProperty("4. close")
    private String close;

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }
}}

