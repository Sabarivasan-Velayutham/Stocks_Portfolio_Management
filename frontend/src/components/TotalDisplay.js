// TotalDisplay.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const TotalDisplay = () => {
  const [totalInvested, setTotalInvested] = useState(null);
  const [totalValue, setTotalValue] = useState(null);
  const [totalProfit, setTotalProfit] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Fetch total invested
    axios.get('/api/stocks/totalInvested')
      .then(response => setTotalInvested(response.data))
      .catch(err => setError(err.message));

    // Fetch total value
    axios.get('/api/stocks/valueTotal')
      .then(response => setTotalValue(response.data))
      .catch(err => setError(err.message));

    // Fetch total profit
    axios.get('/api/stocks/profit')
      .then(response => setTotalProfit(response.data))
      .catch(err => setError(err.message));
  }, []);

  return (
    <div>
      <h3>Total Information</h3>
      {error && <p>Error: {error}</p>}
      <p><strong>Total Invested:</strong> ${totalInvested ? totalInvested.toFixed(2) : 'Loading...'}</p>
      <p><strong>Total Value:</strong> ${totalValue ? totalValue.toFixed(2) : 'Loading...'}</p>
      <p><strong>Total Profit:</strong> ${totalProfit ? totalProfit.toFixed(2) : 'Loading...'}</p>
    </div>
  );
};

export default TotalDisplay;
