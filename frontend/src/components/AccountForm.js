
import React, { useState } from 'react';
import './AccountForm.css';
import axios from './axiosInstance'; 

const AccountForm = () => {
  const [name, setName] = useState('');
  const [number, setNumber] = useState('');
  const [type, setType] = useState('');
  const [balance, setBalance] = useState(0);
  const [interest, setInterest] = useState(0);
  const [message, setMessage] = useState('');

  const handleAddAccount = () => {
    axios.post('/addAccount', { name, number, type, balance, interest })
      .then(response => setMessage(response.data))
      .catch(error => setMessage('Error adding account'));
  };

  return (
    <div className="account-form">
      <h2>Add Account</h2>
      <input 
        type="text" 
        placeholder="Account Name" 
        value={name} 
        onChange={e => setName(e.target.value)} 
      />
      <input 
        type="text" 
        placeholder="Account Number" 
        value={number} 
        onChange={e => setNumber(e.target.value)} 
      />
      <input 
        type="text" 
        placeholder="Account Type" 
        value={type} 
        onChange={e => setType(e.target.value)} 
      />
      <input 
        type="number" 
        placeholder="Balance" 
        value={balance} 
        onChange={e => setBalance(e.target.value)} 
      />
      <input 
        type="number" 
        placeholder="Interest Rate" 
        value={interest} 
        onChange={e => setInterest(e.target.value)} 
      />
      <button onClick={handleAddAccount}>Add Account</button>
      <p>{message}</p>
    </div>
  );
};

export default AccountForm;
