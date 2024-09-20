import {getCookie} from './cookies';
import {updateLobbyList} from './playerlist';

export let socket = null;

export async function connectWebSocket() {
  const token = getCookie('access_token');
  console.log(token);
  try {
  const url = "wss://sbokdz62pc.execute-api.us-west-1.amazonaws.com/production?Authorization="+token;

    socket = new WebSocket(url);
    
    socket.addEventListener('message', function (event) {
      console.log("Raw Response: ",event.data);
      const jsonData = JSON.parse(event.data);
      console.log("ReturnType:  ",jsonData.returnType)
      //TODO: change below to switch case for returnType parsing.
      if(jsonData.returnType === 'error'){
      console.log("Error Message: ",jsonData.error)
      }
      else if(jsonData.returnType ==='lobbyList'){
      console.log("Lobby list inbound: ",jsonData.lobbyList);
      
      }
      else if(jsonData.returnType ==='playerList'){
        console.log("Player list inbound: ",jsonData.playerList);
        updateLobbyList(jsonData);  
      }
      else{
        console.log("Response Data:  ",jsonData.data)
      }
    });
    socket.addEventListener('open', function (event) {
      console.log('Connection Established');
    });

    socket.addEventListener('close', function (event) {
      console.log('WebSocket connection closed');

    });

    socket.addEventListener('error', function (error) {
      console.error('WebSocket error:', error);
      console.error('WebSocket error:', error.data);
    });
  } catch(error){
    console.error("Failed connecting to websocket: ",error)
  }
}
