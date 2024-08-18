import React, { useState, useEffect } from "react";
import axios from "./axiosInstance";
import "./styles/StockList.css";

const StockList = () => {
  const [stocks, setStocks] = useState([]);
  const [search, setSearch] = useState("");
  const [totalPrice, setTotalPrice] = useState(0);
  const [totalProfit, setTotalProfit] = useState(0);

  useEffect(() => {
    axios
      .get("/allStocks")
      .then(async (response) => {
        const fetchedStocks = response.data;

        const stockDataPromises = fetchedStocks.map((stock) =>
          Promise.all([
            axios
              .get(`/amount/${stock.symbol}`)
              .then((result) => {
                const amount = parseFloat(result.data);
                console.log(`Amount invested for ${stock.symbol}: $${amount}`);
                return amount;
              })
              .catch((error) => {
                console.error(
                  `Error fetching amount for ${stock.symbol}:`,
                  error
                );
                return 0;
              }),

            axios
              .get(`/profit/${stock.symbol}`)
              .then((result) => {
                const profit = parseFloat(result.data);
                console.log(`Profit for ${stock.symbol}: $${profit}`);
                return profit;
              })
              .catch((error) => {
                console.error(
                  `Error fetching profit for ${stock.symbol}:`,
                  error
                );
                return 0;
              }),
          ]).then(([amountInvested, profit]) => ({
            ...stock,
            amountInvested,
            profit,
          }))
        );

        const stocksWithData = await Promise.all(stockDataPromises);
        setStocks(stocksWithData);

        const totalInvestment = stocksWithData.reduce(
          (sum, stock) => sum + stock.amountInvested + stock.profit,
          0
        );
        setTotalPrice(totalInvestment);

        const totalProfits = stocksWithData.reduce(
          (sum, stock) => sum + stock.profit,
          0
        );
        setTotalProfit(totalProfits);
      })
      .catch((error) => console.error("Error fetching stocks:", error));
  }, []);

  // Filter stocks based on the search input
  const filteredStocks = stocks.filter((stock) =>
    stock.name.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="stock-list">
      <h1>Your Portfolio</h1>
      <h3>Total Value: $ {Math.abs(totalPrice.toFixed(2))}</h3>
      <br />
      <input
        type="text"
        placeholder="Search by stock ticker..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        className="search-input"
      />
      <ul>
        {filteredStocks.length > 0 ? (
          filteredStocks.map((stock) => (
            <li key={stock.id}>
              <h3>
                {/* {stock.name} ({stock.symbol}) */}
                {stock.symbol}
              </h3>
              <p>Current Price: $ {stock.currentPrice.toFixed(2)}</p>
              <p>Amount Invested: $ {Math.abs(stock.amountInvested.toFixed(2))}</p>
              <p>Profit: ${stock.profit.toFixed(2)}</p>
            </li>
          ))
        ) : (
          <li>No stocks found</li>
        )}
      </ul>
    </div>
  );
};

export default StockList;
