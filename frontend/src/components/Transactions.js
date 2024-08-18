import React, { useState } from "react";
import axios from "./axiosInstance";
import "./styles/Transactions.css";

function Transactions() {
  const [symbol, setSymbol] = useState("");
  const [quantity, setQuantity] = useState(0);
  const [type, setType] = useState("BUY");
  const [number, setNumber] = useState("");

  const handleTransaction = () => {
    const transactionData = { symbol, quantity, type, number };

    axios
      .post("/transaction", transactionData)
      .then((response) => {
        alert(response.data); // Show success message from the backend
      })
      .catch((error) => {
        if (error.response && error.response.data) {
          alert(`Error: ${error.response.data}`); // Show error message from the backend
        } else {
          console.error(error);
          alert("An unexpected error occurred. Please try again.");
        }
      });
  };

  return (
    <div className="manage-transactions">
      <h1>Make a Transaction</h1>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          handleTransaction();
        }}
      >
        <label>
          Symbol:
          <input
            type="text"
            value={symbol}
            onChange={(e) => setSymbol(e.target.value)}
            required
          />
        </label>
        <label>
          Quantity:
          <input
            type="number"
            value={quantity}
            onChange={(e) => setQuantity(e.target.value)}
            required
          />
        </label>
        <label>
          Type:
          <select value={type} onChange={(e) => setType(e.target.value)}>
            <option value="BUY">Buy</option>
            <option value="SELL">Sell</option>
          </select>
        </label>
        <label>
          Account Number:
          <input
            type="text"
            value={number}
            onChange={(e) => setNumber(e.target.value)}
            required
          />
        </label>
        <button type="submit">Submit</button>
      </form>
    </div>
  );
}

export default Transactions;
