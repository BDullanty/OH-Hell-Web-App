import React, { useEffect } from 'react';
import axios from 'axios';
import {Button} from '@mui/material';

const clientId = '1l1nm0q3l9hjvk5hlsi1vhefj3';
const redirectUri = encodeURIComponent('https://main.dmqlib7blr1by.amplifyapp.com/oauth/callback');

  //creates the initial request for the token.
  //We are using PKCE so we must generate a challenge and send it
export const createAuthLink = () =>{
    console.log('in createAuthLink')
    const codeChallenge = localStorage.getItem('challenge');
    const scopes = encodeURIComponent('email openid phone');
    const codeChallengeMethod = 'S256';
    const authUrl = `https://ohhell.auth.us-west-1.amazoncognito.com/login?`+
                    `client_id=${clientId}&` +
                    `response_type=code&` +
                    `scope=${scopes}&` +
                    `redirect_uri=${redirectUri}&` +
                    `code_challenge_method=${codeChallengeMethod}&`+
                    `code_challenge=${codeChallenge}`;

            localStorage.setItem('authUrl', authUrl);
            console.log("Access Auth URL:",localStorage.getItem('authUrl'))
            return authUrl;
};
export function OAuthHandler() {
  console.log('In OAuthHandler');
  
  useEffect(() => {
    const handleOAuthRedirect = async () => {
      const urlParams = new URLSearchParams(window.location.search);
      const authCode = urlParams.get('code');

      if (authCode) {
        console.log('AuthCode:', authCode);
        const codeVerifier = localStorage.getItem('verifier'); // Retrieve the codeVerifier
        
        if (!codeVerifier) {
          console.error('Code verifier not found.');
          return;
        }
        const OAuthURL = `https://ohhell.auth.us-west-1.amazoncognito.com/oauth2/token`;
        console.log("Verifier for token request:\n",codeVerifier);
        const data = new URLSearchParams({
          'client_id': clientId,
          'grant_type': 'authorization_code',
          'code': authCode,
          'code_verifier': codeVerifier,
          'redirect_uri': redirectUri,

        }).toString();
        console.log("Token Request Data:\n",data)
        try {
          const response = await axios.post(
            OAuthURL,
            data,
            {
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
              }
            },
            

          );

          const { access_token, refresh_token } = response.data;
          console.log('Access Token:', access_token);
          console.log('Refresh Token:', refresh_token);
          //lets store it for usage in cookies.
          document.cookie = "access_token="+access_token;
          //now we redirect to add page
          window.location.replace('https://main.dmqlib7blr1by.amplifyapp.com/counter');
        } catch (error) {
          console.error('Token Exchange Error:', error.response ? error.response.data : error.message);
        }
      }
    };
    
    handleOAuthRedirect();
  }, []);

  return <div>
    <p>Handling OAuth Stuff...</p>
    <Button
  onClick={() => {
    window.location.replace('https://main.dmqlib7blr1by.amplifyapp.com/');
  }}
>
  Click me to go to home page
</Button>

  
  </div>
};


function generateVerifier(length) {
  var text = "";
  var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  for (var i = 0; i < length; i++) {
    text += possible.charAt(Math.floor(Math.random() * possible.length));
  }

  return text;
}
async function generateCodeChallenge(codeVerifier) {
  var digest = await crypto.subtle.digest("SHA-256",
    new TextEncoder().encode(codeVerifier));

  return btoa(String.fromCharCode(...new Uint8Array(digest)))
    .replace(/=/g, '').replace(/\+/g, '-').replace(/\//g, '_')
}

export async function generateChallenge(){
  
  const verifier = generateVerifier(128);
  const challenge = await generateCodeChallenge(verifier);
  localStorage.setItem('verifier', verifier);
  localStorage.setItem('challenge', challenge);
  console.log('Logged verifier',localStorage.getItem('verifier'));
  console.log('Logged challenge',localStorage.getItem('challenge'));
  const returnValue = createAuthLink();
  return returnValue;
}
