import React, { useEffect } from 'react';
import axios from 'axios';
import pkceChallenge from 'pkce-challenge';
import { verifyChallenge } from "pkce-challenge";
 

  //creates the initial request for the token.
  //We are using PKCE so we must generate a challenge and send it
  export const createAuthLink = () =>{
    console.log('in createAuthLink')
    console.log('Verifier initially:'+localStorage.getItem('verifier'))
    const codeChallenge = localStorage.getItem('challenge');
    const clientId = '44i7gporhs72lcdgs0ht1ak564';
    const redirectUri = encodeURIComponent('https://main.dmqlib7blr1by.amplifyapp.com/oauth/callback');
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
      const redirectUri = 'https://main.dmqlib7blr1by.amplifyapp.com/oauth/callback';

      if (authCode) {
        console.log('AuthCode:', authCode);
        const codeVerifier = localStorage.getItem('verifier'); // Retrieve the codeVerifier
        console.log('OAuth Verifier:', codeVerifier);
        
        if (!codeVerifier) {
          console.error('Code verifier not found.');
          return;
        }
        const OAuthURL = `https://ohhell.auth.us-east-1.amazoncognito.com/oauth2/token?`;
        console.log("Verifier for token request:",codeVerifier);
        checkIfValid();
        const data = new URLSearchParams({
          client_id: '44i7gporhs72lcdgs0ht1ak564',
          grant_type: 'authorization_code',
          code: authCode,
          code_verifier: codeVerifier,
          redirect_uri: redirectUri,

        });
        console.log(OAuthURL)
        console.log(data)
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
        } catch (error) {
          console.error('Token Exchange Error:', error.response ? error.response.data : error.message);
        }
      }
    };
    
    handleOAuthRedirect();
  }, []);
  return <div>Handling OAuth...</div>;
};
export async function checkIfValid(){
  try{
    const result = await verifyChallenge(localStorage.getItem('verifier'), localStorage.getItem('challenge')) ===
  true // true){
    console.log("Challenge is valid. ",result)
  } catch(error){
    console.log('Challenge FAILED');
  }
}
export async function generateChallenge(){
  
  const response = await pkceChallenge();
  console.log('Generating Challenge',response);
  localStorage.setItem('verifier',response.code_verifier);
  localStorage.setItem('challenge',response.code_challenge);
  console.log('Logged verifier',localStorage.getItem('verifier'));
  createAuthLink();
}
