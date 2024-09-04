import {getCookie} from './cookies';


const url = 'wss://ylhdgko44i.execute-api.us-west-1.amazonaws.com/production';

let socket = null;
export async function connectWebSocket() {
  const token = getCookie('access_token');
  try {
  console.log("Cookie: ",token)
  console.log("Connection URL: ",url);

    socket =new WebSocket(url);

    socket.addEventListener('open', function (event) {
      console.log('WebSocket connection opened',event.data);
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