import axios from 'axios';

// Create an instance of axios with the base URL
const instance = axios.create({
  baseURL: 'http://localhost:8080/api/stocks',
});

export default instance;
