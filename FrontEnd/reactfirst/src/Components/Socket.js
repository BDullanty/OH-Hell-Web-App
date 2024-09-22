
import { getCookie } from './cookies';

export let socket = null;

export async function connectWebSocket(setUsers,setGames) {
  const token = getCookie('access_token');
  console.log(token);
  try {
    const url = "wss://sbokdz62pc.execute-api.us-west-1.amazonaws.com/production?Authorization=" + token;

    socket = new WebSocket(url);

    socket.addEventListener('message', function (event) {
      console.log("Raw Response: ", event.data);
      const jsonData = JSON.parse(event.data);
      console.log("ReturnType:  ", jsonData.returnType)

      switch (jsonData.returnType) {
        case 'lobbyList':
         // setLobby(jsonData.data.lobbyList);
          break;
        case 'playerList':
          setUsers(jsonData.users);
          break;
        case 'gameList':
            setGames(jsonData.games);
            break;
        case 'createGame':
            console.log("createGame response triggered");
            break;
        case 'error':
          console.error("Error Message: ", jsonData.error);
          break;
        default:
          console.log("Response Data: ", jsonData.data);
          break;
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
  } catch (error) {
    console.error("Failed connecting to websocket: ", error)
  }
}
