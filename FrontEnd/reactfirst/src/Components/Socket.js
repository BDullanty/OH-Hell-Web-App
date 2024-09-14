import {getCookie} from './cookies';

export let socket = null;
export async function connectWebSocket() {
  const token = getCookie('access_token');
  try {
  const url = "wss://sbokdz62pc.execute-api.us-west-1.amazonaws.com/production?Authorization="+token;

    socket = new WebSocket(url);

    socket.addEventListener('open', function (event) {
      console.log('Connection Established');
    });

    socket.addEventListener('message', function (event) {
      console.log("Raw Response: ",event.data);
      const jsonData = JSON.parse(event.data);
      //TODO: change below to switch case for returnType parsing.
      if(jsonData.returnType === 'error'){
      console.log("ReturnType: error, message: ",jsonData.error)
      }
      else{
      }
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