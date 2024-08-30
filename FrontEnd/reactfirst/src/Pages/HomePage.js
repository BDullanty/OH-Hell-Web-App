
import '../App.css';
import React, { useState, useEffect } from 'react';
import {generateChallenge} from '../Components/OAuthHandler';
import {Button} from '@mui/material';
const HomePage = () =>{
  
  const [authUrl, setAuthUrl] = useState(''); // Initialize state with empty string

  useEffect(() => {
    const fetchAuthUrl = async () => {
      try {
        const url = await generateChallenge(128); // Await the async function
        setAuthUrl(url); // Update state with the resolved URL
      } catch (error) {
        console.error('Error generating challenge:', error);
      }
    };

    fetchAuthUrl(); // Call the async function when component mounts
  }, []); // Empty dependency array to run only once on mount


  return (
    <div className="App">    

    <header className="App-header">
  <p>
  Click Below to Sign In.
  </p>

  <Button
  onClick={() => {
    window.location.replace(authUrl);
  }}
>
  Login
</Button>
    </header>
  </div>

  );
}
export default HomePage;
