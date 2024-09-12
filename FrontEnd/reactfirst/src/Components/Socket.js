import {getCookie} from './cookies';



export let socket = null;
export async function connectWebSocket() {
  const token = getCookie('access_token');
  try {
  const url = "wss://ylhdgko44i.execute-api.us-west-1.amazonaws.com/production?Authorization="+token;
  

    socket = new WebSocket(url);

    socket.addEventListener('open', function (event) {
      console.log('WebSocket connection opened :)');
    });

    socket.addEventListener('message', function (event) {
      const jsonData = JSON.parse(event.data);
      console.log('Message from server:', jsonData);
      if(jsonData.returnType === 'error'){
      console.log("Bad Request",jsonData)
      }
      else if(jsonData.returnType === 'connect'){
      console.log("connect response:",jsonData)
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