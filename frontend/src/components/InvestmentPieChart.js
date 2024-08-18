import React, { useState, useEffect } from "react";
import { Pie } from "react-chartjs-2";
import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement } from "chart.js";
import axios from "axios";
import "./styles/InvestmentPieChart.css";

ChartJS.register(Title, Tooltip, Legend, ArcElement);

const InvestmentPieChart = () => {
  const [chartData, setChartData] = useState({ labels: [], datasets: [] });
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchData = () => {
      axios
        .get("http://localhost:8080/api/stocks/allStocks")
        .then((response) => {
          const stocks = response.data;
          console.log("Fetched Stocks:", stocks); // Debugging

          const amountsInvestedPromises = stocks.map((stock) =>
            axios
              .get(`http://localhost:8080/api/stocks/amount/${stock.symbol}`)
              .then((result) => {
                const amount = parseFloat(result.data); // Convert to float
                console.log(amount);
                return {
                  symbol: stock.symbol,
                  amountInvested: amount, // Convert to absolute value or 0 if NaN
                };
              })
              .catch((error) => {
                console.error(
                  `Error fetching amount for ${stock.symbol}:`,
                  error
                );
                return { symbol: stock.symbol, amountInvested: 0 }; // Default to 0 on error
              })
          );

          // Handling the amountsInvested data after all promises resolve
          Promise.all(amountsInvestedPromises)
            .then((amountsInvested) => {
              const labels = amountsInvested.map((item) => item.symbol);
              const data = amountsInvested.map((item) => item.amountInvested);

              console.log("Chart Labels:", labels); // Debugging
              console.log("Chart Data:", data); // Debugging

              setChartData({
                labels,
                datasets: [
                  {
                    label: "Amount Invested",
                    data,
                    backgroundColor: data.map(
                      (_, index) =>
                        `rgba(${index * 50}, ${index * 50}, 200, 0.6)`
                    ),
                    borderColor: "rgba(0, 0, 0, 0.1)",
                    borderWidth: 1,
                  },
                ],
              });

              setIsLoading(false);
            })
            .catch((error) => {
              console.error("Error processing investment data:", error);
              setIsLoading(false);
            });
        })
        .catch((error) => {
          console.error("Error fetching stock data:", error);
          setIsLoading(false);
        });
    };

    fetchData();
  }, []);

  return (
    <div>
      <h4>Stock Investment Distribution</h4>
      {isLoading ? (
        <p>Loading...</p>
      ) : (
        <div style={{ width: "100%", height: "400px" }}>
          <Pie data={chartData} />
        </div>
      )}
    </div>
  );
};

export default InvestmentPieChart;
