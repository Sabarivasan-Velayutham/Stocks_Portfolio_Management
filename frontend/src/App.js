import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import StockList from "./components/StockList";
import StockDetails from "./components/StockDetails";
import Transactions from "./components/Transactions";
import AccountList from "./components/AccountList";
import Recommendations from "./components/Recommendations";
import "./App.css";
import TransactionHistory from "./components/TransactionHistory";
import Dashboard from "./components/Dashboard";

const App = () => {
  return (
    <Router>
      <div className="app">
        <nav>
          <ul>
            <li>
              <a href="/stocks">Home</a>
            </li>
            <li>
              <a href="/dashboard">Dashboard</a>
            </li>
          
            <li>
              <a href="/transactions">Buy / Sell</a>
            </li>
            
            <li>
              <a href="/recommendations">Recommendations</a>
            </li>
            <li>
              <a href="/transaction-history">Transaction History</a>
            </li>
            <li>
              <a href="/accounts">Profile</a>
            </li>
          </ul>
        </nav>
        <Routes>
          <Route path="/" element={<StockList />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/stocks" element={<StockList />} />
          <Route path="/stocks/:symbol" element={<StockDetails />} />
          <Route path="/accounts" element={<AccountList />} />
          <Route path="/transactions" element={<Transactions />} />
          <Route path="/transaction-history" element={<TransactionHistory />} />
          <Route path="/recommendations" element={<Recommendations />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
