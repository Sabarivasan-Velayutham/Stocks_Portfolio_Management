import React, { useState, useEffect } from "react";
import axios from "./axiosInstance";
import "./styles/Recommendations.css";

const Recommendations = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchRecommendations = async () => {
      try {
        const response = await axios.get("/recommendations");
        setData(response.data);
      } catch (error) {
        console.error("Error fetching recommendations:", error);
        setError("Failed to load data");
      } finally {
        setLoading(false);
      }
    };
    fetchRecommendations();
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  if (
    !data ||
    !data.top_gainers ||
    !data.top_losers ||
    !data.most_actively_traded
  ) {
    return <div>No data available</div>;
  }

  return (
    <div className="recommendations">
      <h1>Top Performing Stocks</h1>

      <section>
        <h2>Top Gainers</h2>
        <ul>
          <li id="table-header">
            <span>Ticker</span>
            <span>Price</span>
            <span>Change in (Amount , %)</span>
            <span>Volume</span>
          </li>
          {data.top_gainers.map((stock, index) => (
            <li key={index}>
              <strong>{stock.ticker}</strong> ${stock.price}
              <span className="change">
                {" "}
                ({stock.change_amount}, {stock.change_percentage})
              </span>
              <span className="volume">{stock.volume}</span>
            </li>
          ))}
        </ul>
      </section>

      <section>
        <h2>Top Losers</h2>
        <ul>
          <li id="table-header">
            <span>Ticker</span>
            <span>Price</span>
            <span>Change in (Amount , %)</span>
            <span>Volume</span>
          </li>
          {data.top_losers.map((stock, index) => (
            <li key={index}>
              <strong>{stock.ticker}</strong> ${stock.price}
              <span className="change">
                {" "}
                ({stock.change_amount}, {stock.change_percentage})
              </span>
              <span className="volume">{stock.volume}</span>
            </li>
          ))}
        </ul>
      </section>

      <section>
        <h2>Most Actively Traded</h2>
        <ul>
          <li id="table-header">
            <span>Ticker</span>
            <span>Price</span>
            <span>Change in (Amount , %)</span>
            <span>Volume</span>
          </li>
          {data.most_actively_traded.map((stock, index) => (
            <li key={index}>
              <strong>{stock.ticker}</strong> ${stock.price}
              <span className="change">
                {" "}
                ({stock.change_amount}, {stock.change_percentage})
              </span>
              <span className="volume">{stock.volume}</span>
            </li>
          ))}
        </ul>
      </section>
    </div>
  );
};

export default Recommendations;
