import React, { useState, useEffect } from "react";
import axios from "./axiosInstance";
import "./styles/TransactionHistory.css";

const TransactionHistory = () => {
  const [transactions, setTransactions] = useState([]);

  useEffect(() => {
    axios
      .get("/transactions")
      .then((response) => {
        // Sort transactions by id (most recent first)
        const sortedTransactions = response.data.sort((a, b) => b.id - a.id);
        setTransactions(sortedTransactions);
      })
      .catch((error) => console.error("Error fetching transactions:", error));
  }, []);

  const formatDate = (timestamp) => {
    const [year, month, day, hour, minute] = timestamp;
    return new Date(year, month - 1, day, hour, minute).toLocaleString();
  };

  return (
    <div className="transaction-history">
      <h2>Transaction History</h2>
      <table>
        <thead>
          <tr>
            <th>Stock Symbol</th>
            <th>Quantity</th>
            <th>Price</th>
            <th>Date</th>
            <th>Type</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map((transaction) => (
            <tr key={transaction.id}>
              <td>{transaction.stock.symbol}</td>
              <td>{transaction.quantity}</td>
              <td>{transaction.price.toFixed(2)}</td>
              <td>{formatDate(transaction.timestamp)}</td>
              <td>{transaction.type}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default TransactionHistory;
