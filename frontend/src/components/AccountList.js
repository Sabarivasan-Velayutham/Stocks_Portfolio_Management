import React, { useState, useEffect } from 'react';
import axios from './axiosInstance'; 
import './styles/AccountList.css';

const AccountList = () => {
  const [accounts, setAccounts] = useState([]);

  useEffect(() => {
    const fetchAccounts = async () => {
      try {
        const response = await axios.get('/accounts');
        setAccounts(response.data);
      } catch (error) {
        console.error('Error fetching accounts:', error);
      }
    };
    fetchAccounts();
  }, []);

  return (
    <div className="account-list">
      <h1>Profile</h1>
      <ul>
        {accounts.map(account => (
          <li key={account.number}>
            <p>Name: {account.name}</p>
            <p>Number: {account.number}</p>
            <p>Type: {account.type}</p>
            <p>Balance: ${account.balance}</p>
            <p>Interest: {account.interest}%</p>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default AccountList;
