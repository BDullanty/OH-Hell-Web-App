const url = 'wss://ohzd43i31j.execute-api.us-west-1.amazonaws.com/production/';

let socket = null;

export async function connectWebSocket() {
    try {
      const token = document.cookie;
  console.log("Cookie: ",document.cookie)

    socket =new WebSocket(url, [], {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

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
    });
  } catch(error){
    console.error("Failed connecting to websocket: ",error)
  }
}