import React, { useState, useEffect } from 'react';
import axios from './axiosInstance'; 
import './styles/StockDetails.css';

const StockDetails = ({ match }) => {
  const [stock, setStock] = useState(null);
  const { symbol } = match.params;

  useEffect(() => {
    const fetchStock = async () => {
      try {
        const response = await axios.get(`/${symbol}`);
        setStock(response.data);
      } catch (error) {
        console.error('Error fetching stock details:', error);
      }
    };
    fetchStock();
  }, [symbol]);

  if (!stock) return <div>Loading...</div>;

  return (
    <div className="stock-details">
      <h1>{stock.symbol}</h1>
      <p>Name: {stock.name}</p>
      <p>Price: ${stock.price}</p>
      <p>Market Cap: ${stock.marketCap}</p>
      <p>Volume: {stock.volume}</p>
    </div>
  );
};

export default StockDetails;
