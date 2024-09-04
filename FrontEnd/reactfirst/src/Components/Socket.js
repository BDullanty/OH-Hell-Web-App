import {getCookie} from './cookies';


let url = 'wss://ylhdgko44i.execute-api.us-west-1.amazonaws.com/production';

let socket = null;
export async function connectWebSocket() {
  const token = getCookie('access_token');
  try {
  console.log("Cookie: ",token)
  console.log("Connection URL: ",url);
  url +="?Authorization="+token;
  console.log("URL and Token: ",url);

    socket = new WebSocket(url);

    socket.addEventListener('open', function (event) {
      console.log('WebSocket connection opened :)',event);
    });

    socket.addEventListener('message', function (event) {
      console.log('Message from server:', event.data);
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