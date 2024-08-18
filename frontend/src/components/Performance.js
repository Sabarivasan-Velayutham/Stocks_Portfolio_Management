import React, { useState, useEffect } from 'react';
import axios from './axiosInstance'; // Adjust import based on your setup
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, PointElement, CategoryScale, LinearScale } from 'chart.js';

// Registering Chart.js components
ChartJS.register(Title, Tooltip, Legend, LineElement, PointElement, CategoryScale, LinearScale);

const Performance = ({ stockSymbols }) => {  // Accept a list of stock symbols as input
    const [chartData, setChartData] = useState({});
    const apiKey = 'I7JDO8RJB15FE98T'; // Replace with your API key
  
    useEffect(() => {
        const fetchStockPerformanceData = async () => {
            const dataPromises = stockSymbols.map(symbol => {
                const url = `https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol=${symbol}&apikey=${apiKey}`;
                return axios.get(url)
                    .then(response => {
                        console.log(`Data for ${symbol}:`, response.data);
                        return {
                            symbol: symbol,
                            data: response.data['Monthly Adjusted Time Series'],
                        };
                    })
                    .catch(error => {
                        console.error(`Error fetching data for ${symbol}:`, error);
                        return { symbol: symbol, data: {} };
                    });
            });
      
            const results = await Promise.all(dataPromises);
            console.log('Results:', results); // Debugging
      
            const newChartData = results.reduce((acc, { symbol, data }) => {
                if (data) {
                    // Filter data to only include entries from 2024 onwards
                    const filteredData = Object.entries(data)
                        .filter(([date]) => new Date(date).getFullYear() >= 2024)
                        .reduce((obj, [date, value]) => {
                            obj[date] = value;
                            return obj;
                        }, {});
      
                    const labels = Object.keys(filteredData).reverse();
                    const prices = labels.map(label => filteredData[label]['5. adjusted close']).reverse();
                    acc[symbol] = { labels, data: prices };
                }
                return acc;
            }, {});
      
            setChartData(newChartData);
        };
      
        if (stockSymbols.length > 0) {
            fetchStockPerformanceData();
        }
    }, [stockSymbols]);
  
    // Prepare data for the line chart
    const chartOptions = {
        responsive: true,
        plugins: {
            legend: {
                position: 'top',
            },
            tooltip: {
                callbacks: {
                    label: function (tooltipItem) {
                        return `Price: $${tooltipItem.raw.toFixed(2)}`;
                    },
                },
            },
        },
        scales: {
            x: {
                title: {
                    display: true,
                    text: 'Date',
                },
                ticks: {
                    autoSkip: true,
                    maxTicksLimit: 10,
                },
            },
            y: {
                title: {
                    display: true,
                    text: 'Price',
                },
                ticks: {
                    callback: function (value) {
                        return `$${value}`;
                    },
                },
            },
        },
    };
  
    const chartDataForLineChart = {
        labels: chartData[stockSymbols[0]]?.labels || [],  // X-axis labels (dates)
        datasets: stockSymbols.map(symbol => {
            const data = chartData[symbol]?.data || [];
            
            // Log the coordinates for each point
            data.forEach((value, index) => {
                console.log(`Symbol: ${symbol}, Date: ${chartData[stockSymbols[0]].labels[index]}, Price: $${value}`);
            });
    
            return {
                label: symbol,  // The name of the stock symbol, used in the legend
                data: data,  // Y-axis data (stock prices)
                borderColor: `rgba(${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, 1)`,  // Line color
                backgroundColor: `rgba(${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, 0.2)`,  // Fill color under the line
                borderWidth: 2,  // Thickness of the line
                fill: true,  // Whether to fill under the line
            };
        }),
    };
    
    return (
        <div className="stock-performance-chart">
            <h4>Stock Performance Over Time</h4>
            <div style={{ width: '120%', height: '300px' }}>
                <Line data={chartDataForLineChart} options={chartOptions} />
            </div>
        </div>
    );
};

export default Performance;
