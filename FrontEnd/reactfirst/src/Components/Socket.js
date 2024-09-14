import {getCookie, setCookie} from './cookies';



export let socket = null;
export async function connectWebSocket() {
  const token = getCookie('access_token');
  try {
  const url = "wss://sbokdz62pc.execute-api.us-west-1.amazonaws.com/production?Authorization="+token;
  

    socket = new WebSocket(url);

    socket.addEventListener('open', function (event) {
      console.log('WebSocket connection opened :)');
    });

    socket.addEventListener('message', function (event) {
      console.log(event.data);
      const jsonData = JSON.parse(event.data);

      if(jsonData.returnType === 'error'){
      console.log("Bad Request",jsonData.error)
      }
      else if(jsonData.returnType === 'connect'){
      console.log("connect response:",jsonData)
      setCookie("sub",jsonData.sub,1);
      setCookie("username",jsonData.username,1);
      console.log("Sub logged as: ", getCookie("sub"));
      console.log("username logged as: ", getCookie("username"));
      }
      else{
      console.log('Message from server:', jsonData);
      }
    });

    socket.addEventListener('close', function (event) {
      console.log('WebSocket connection closed');
      console.log('Message from server: ',event.data);
    });

    socket.addEventListener('error', function (error) {
      console.error('WebSocket error:', error);
      console.error('WebSocket error:', error.data);
    });
  } catch(error){
    console.error("Failed connecting to websocket: ",error)
  }
}