import React, { useEffect, useState } from 'react';
import axios from './axiosInstance'; 
import './styles/Account.css';

function Account() {
  const [accounts, setAccounts] = useState([]);

  useEffect(() => {
    axios.get('/accounts') 
      .then(response => setAccounts(response.data))
      .catch(error => console.error(error));
  }, []);

  return (
    <div className="account">
      <h1>Account Details</h1>
      <table>
        <thead>
          <tr>
            <th>Account Number</th>
            <th>Type</th>
            <th>Balance</th>
            <th>Last Interest Applied</th>
          </tr>
        </thead>
        <tbody>
          {accounts.map(account => (
            <tr key={account.number}>
              <td>{account.number}</td>
              <td>{account.type}</td>
              <td>{account.balance}</td>
              <td>{account.lastInterestApplied}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Account;
