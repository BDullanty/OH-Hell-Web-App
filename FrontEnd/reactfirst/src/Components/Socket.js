
import { getCookie, setCookie } from './cookies';

export let socket = null;

export async function connectWebSocket(setUsers, setGames) {
  const token = getCookie('access_token');
  console.log(token);
  try {
    const url = "wss://sbokdz62pc.execute-api.us-west-1.amazonaws.com/production?Authorization=" + token;

    socket = new WebSocket(url);

    socket.addEventListener('message', function (event) {


      const jsonData = JSON.parse(event.data);
      switch (jsonData.returnType) {
        case 'users':
          console.log("User List Inbound");
          setUsers(jsonData.users);
          break;
        case 'gameList':
          console.log("Received game list");
          setGames(jsonData.games);
          break;
        case 'userInfo':
          console.log("User info received");
          setCookie('userName',jsonData.userName);
          setCookie('userState',jsonData.userState);
          setCookie('gameID',jsonData.gameID);
          break;
        case 'createGame':
          setCookie('gameID',jsonData.gameID);
          console.log("createGame response triggered. returned Game ID: "+getCookie('gameID'));
          break;
        case 'startGame':
          console.log("startGame response triggered");
          break;
        case 'error':
          console.error("Error Message: ", jsonData.error);
          break;
        default:
          console.log("Response Data: ", jsonData.data);
          break;
        }
        console.log("Raw Response: ", event.data);
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
export const voteStart = () => {
  const voteStartMessage =
    JSON.stringify(
      {
      action: "VoteStart"
    }
  );
  console.log(voteStartMessage)
    socket.send(voteStartMessage);


}
export const leaveGame = () => {
  const leaveGameMessage =
    JSON.stringify(
      {
      action: "LeaveGame"
    }
  );
}

export const joinGame = (gameID) => {
  const leaveGameMessage =
    JSON.stringify(
      {
      action: "JoinGame",
      gameID: gameID
    }
  );
console.log(leaveGameMessage)
  socket.send(leaveGameMessage);
}