import React, { useState, useEffect } from 'react'; // Import hooks
import axios from './axiosInstance'; // Adjust this import based on your setup
import Performance from './Performance';
import InvestmentPieChart from './InvestmentPieChart';
import './styles/Dashboard.css'; // Import the CSS file

const Dashboard = () => {
  const [stocks, setStocks] = useState([]);

  useEffect(() => {
    axios.get('/allStocks') 
      .then(response => {
        setStocks(response.data); // Assuming the response is an array of stocks
      })
      .catch(error => {
        console.error('Error fetching stocks:', error);
      });
  }, []);

  // Extract stock symbols
  const sym = stocks.map(stock => stock.symbol);

  return (

    <div className="container">
      
      <div className="stock-pie-chart">
        <InvestmentPieChart />
      </div>
      <div className="stock-performance-chart">
        <Performance stockSymbols={sym} />
      </div>
    </div>
  );
};

export default Dashboard;
