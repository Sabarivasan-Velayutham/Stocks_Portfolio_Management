import React, { useEffect, useState } from 'react';
import './styles/Portfolio.css';
import axios from './axiosInstance'; 

function Portfolio() {
  const [stocks, setStocks] = useState([]);

  useEffect(() => {
    axios.get('/api/stocks') 
      .then(response => setStocks(response.data))
      .catch(error => console.error(error));
  }, []);

  return (
    <div className="portfolio">
      <h1>Portfolio</h1>
      <table>
        <thead>
          <tr>
            <th>Symbol</th>
            <th>Name</th>
            <th>Current Price</th>
          </tr>
        </thead>
        <tbody>
          {stocks.map(stock => (
            <tr key={stock.symbol}>
              <td>{stock.symbol}</td>
              <td>{stock.name}</td>
              <td>{stock.currentPrice}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Portfolio;
