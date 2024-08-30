import React, { useEffect } from 'react';
import axios from 'axios';
import pkceChallenge from 'pkce-challenge';
import { verifyChallenge } from "pkce-challenge";
 

  //creates the initial request for the token.
  //We are using PKCE so we must generate a challenge and send it
  export const createAuthLink = () =>{
    console.log('in createAuthLink')
    const codeChallenge = localStorage.getItem('challenge');
    const clientId = '6i4uih2m1usmsbkrp316qhpdsl';
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
            console.log("Access Auth URL:",localStorage.getItem('authUrl'))
            return authUrl;
};
export function OAuthHandler() {
  console.log('In OAuthHandler');
  
  useEffect(() => {
    const handleOAuthRedirect = async () => {
      const urlParams = new URLSearchParams(window.location.search);
      const authCode = urlParams.get('code');
      const redirectUri = 'http://localhost:3000/oauth/callback';

      if (authCode) {
        console.log('AuthCode:', authCode);
        const codeVerifier = localStorage.getItem('verifier'); // Retrieve the codeVerifier
        
        if (!codeVerifier) {
          console.error('Code verifier not found.');
          return;
        }
        const OAuthURL = `https://ohhell.auth.us-east-1.amazoncognito.com/oauth2/token`;
        console.log("Verifier for token request:\n",codeVerifier);
        const data = new URLSearchParams({
          'client_id': '6i4uih2m1usmsbkrp316qhpdsl',
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
          //now we redirect to add page
          window.location.replace('http://localhost:3000/counter');
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
    window.location.replace('http://localhost:3000');
  }}
>
  Click me to go to home page
</Button>

  
  </div>
};

}
export async function generateChallenge(){
  
  const response = await pkceChallenge(128);
  console.log('Generating Challenge',response);
  localStorage.setItem('verifier',response.code_verifier);
  localStorage.setItem('challenge',response.code_challenge);
  console.log('Logged verifier',localStorage.getItem('verifier'));
  createAuthLink();
}
