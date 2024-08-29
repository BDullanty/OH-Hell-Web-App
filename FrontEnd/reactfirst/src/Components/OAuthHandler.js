import React, { useEffect } from 'react';
import axios from 'axios';
generateCodeVerifier();
generateCodeChallenge();

  export const createAuthLink = async()=> {
    console.log('in create auth link')
    const verifier = localStorage.getItem('verifier');
    localStorage.setItem('code_verifier', verifier);
    console.log('Verifier initially:'+localStorage.getItem('verifier'))
    const codeChallenge = localStorage.getItem('challenge');
    const clientId = '44i7gporhs72lcdgs0ht1ak564';
    const redirectUri = encodeURIComponent('http://localhost:3000/oauth/callback');
    const scopes = encodeURIComponent('email openid phone');
    const codeChallengeMethod = 'S256';
    console.log()
    const authUrl = `https://ohhell.auth.us-east-1.amazoncognito.com/login?`+
                    `client_id=${clientId}&` +
                    `response_type=code&` +
                    `scope=${scopes}&` +
                    `redirect_uri=${redirectUri}&` +
                    `code_challenge_method=${codeChallengeMethod}&`+
                    `code_challenge=${codeChallenge}`;

            localStorage.setItem('authUrl', authUrl);
            console.log(localStorage.getItem('authUrl'))
};
export const OAuthHandler = () => {
  console.log('In OAuthHandler');
  
  useEffect(() => {
    const handleOAuthRedirect = async () => {
      const urlParams = new URLSearchParams(window.location.search);
      const authCode = urlParams.get('code');
      const redirectUri = encodeURIComponent('http://localhost:3000'); // Should match the one used in the initial request

      if (authCode) {
        console.log('AuthCode:', authCode);
        const codeVerifier = localStorage.getItem('code_verifier'); // Retrieve the codeVerifier
        console.log('OAuth Verifier:', codeVerifier);
        
        if (!codeVerifier) {
          console.error('Code verifier not found.');
          return;
        }
        const OAuthURL = `https://ohhell.auth.us-east-1.amazoncognito.com/oauth2/token?`+
                          `grant_type=authorization_code&`+
                          `code=${authCode}&`+
                          `client_id=44i7gporhs72lcdgs0ht1ak564&`+
                          `redirect_uri=${redirectUri}&`+
                          `code_verifier=${codeVerifier}`;
        try {
          const response = await axios.post(
            OAuthURL,
            {
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
              },
            }
          );

          const { access_token, refresh_token } = response.data;
          console.log('Access Token:', access_token);
          console.log('Refresh Token:', refresh_token);
        } catch (error) {
          console.error('Token Exchange Error:', error.response ? error.response.data : error.message);
        }
      }
    };
    
    handleOAuthRedirect();
  }, []);
  return <div>Handling OAuth...</div>;
};
function dec2hex(dec) {
  return ("0" + dec.toString(16)).substr(-2);
}

function generateCodeVerifier() {
  var array = new Uint32Array(56 / 2);
  window.crypto.getRandomValues(array);
  
  localStorage.setItem('verifier',Array.from(array, dec2hex).join(""));
  console.log('Logged verifier',localStorage.getItem('verifier'));
 
}
function sha256(plain) {
  // returns promise ArrayBuffer
  const encoder = new TextEncoder();
  const data = encoder.encode(plain);
  return window.crypto.subtle.digest("SHA-256", data);
}

function base64urlencode(a) {
  var str = "";
  var bytes = new Uint8Array(a);
  var len = bytes.byteLength;
  for (var i = 0; i < len; i++) {
    str += String.fromCharCode(bytes[i]);
  }
  return btoa(str)
    .replace(/\+/g, "-")
    .replace(/\//g, "_")
    .replace(/=+$/, "");
}

async function generateCodeChallenge(v) {
  var hashed = await sha256(v);
  var base64encoded = base64urlencode(hashed);
  localStorage.setItem("challenge",base64encoded);
}