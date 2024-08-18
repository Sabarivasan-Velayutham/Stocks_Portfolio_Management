
import React, { useState } from 'react';
import axios from './axiosInstance'; 
import './FinancialSummary.css';
import axios from './axiosInstance'; 

const FinancialSummary = () => {
  const [symbol, setSymbol] = useState('');
  const [quantity, setQuantity] = useState(0);
  const [profit, setProfit] = useState(null);
  const [value, setValue] = useState(null);
  const [totalInvested, setTotalInvested] = useState(null);
  const [totalValue, setTotalValue] = useState(null);

  const handleGetData = () => {
    axios.get(`/amount/${symbol}`)
      .then(response => setTotalInvested(response.data))
      .catch(error => console.error('Error fetching total invested:', error));
    
    axios.get(`/value/${symbol}`)
      .then(response => setValue(response.data))
      .catch(error => console.error('Error fetching current value:', error));

    axios.get(`/profit/${symbol}`)
      .then(response => setProfit(response.data))
      .catch(error => console.error('Error fetching profit:', error));

      axios.get('/totalInvested')
      .then(response => setTotalInvested(response.data))
      .catch(error => console.error('Error fetching total invested:', error));

    axios.get('/valueTotal')
      .then(response => setTotalValue(response.data))
      .catch(error => console.error('Error fetching total value:', error));
  };

  return (
    <div className="financial-summary">
      <h2>Financial Summary</h2>
      <input 
        type="text" 
        placeholder="Stock Symbol" 
        value={symbol} 
        onChange={e => setSymbol(e.target.value)} 
      />
      <input 
        type="number" 
        placeholder="Quantity" 
        value={quantity} 
        onChange={e => setQuantity(e.target.value)} 
      />
      <button onClick={handleGetData}>Get Financial Data</button>
      <div className="results">
        {totalInvested !== null && <p>Total Invested: ${totalInvested.toFixed(2)}</p>}
        {value !== null && <p>Current Value: ${value.toFixed(2)}</p>}
        {profit !== null && <p>Profit: ${profit.toFixed(2)}</p>}
      </div>
    </div>
  );
};

export default FinancialSummary;

