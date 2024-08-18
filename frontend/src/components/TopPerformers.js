// src/components/TopPerformers.js

import React, { useState, useEffect } from 'react';
// import axios from 'axios';
import axios from './axiosInstance'; 
import './TopPerformers.css';

const TopPerformers = () => {
  const [gainers, setGainers] = useState([]);
  const [losers, setLosers] = useState([]);
  const [recommendations, setRecommendations] = useState([]);

  useEffect(() => {
    axios.get('/gainerloser')
      .then(response => {
        const { gainer, loser } = response.data;
        setGainers(gainer);
        setLosers(loser);
      })
      .catch(error => console.error('Error fetching top gainers and losers:', error));

    axios.get('/api/stocks/recommendations')
      .then(response => setRecommendations(response.data))
      .catch(error => console.error('Error fetching top recommendations:', error));
  }, []);

  return (
    <div className="top-performers">
      <h2>Top Performers</h2>
      <div className="performers">
        <div className="gainers">
          <h3>Top Gainers</h3>
          <ul>
            {gainers.map(stock => (
              <li key={stock.symbol}>
                <p>{stock.name} ({stock.symbol})</p>
              </li>
            ))}
          </ul>
        </div>
        <div className="losers">
          <h3>Top Losers</h3>
          <ul>
            {losers.map(stock => (
              <li key={stock.symbol}>
                <p>{stock.name} ({stock.symbol})</p>
              </li>
            ))}
          </ul>
        </div>
        <div className="recommendations">
          <h3>Top 10 Recommendations</h3>
          <ul>
            {recommendations.map(stock => (
              <li key={stock.symbol}>
                <p>{stock.name} ({stock.symbol})</p>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default TopPerformers;
