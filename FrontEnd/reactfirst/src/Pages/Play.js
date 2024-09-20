
import { Card,CardContent,CardActions, Button } from '@mui/material';
import { connectWebSocket, socket } from '../Components/Socket';
import { getCookie } from '../Components/cookies';
import {playerList} from '../Components/playerlist';
const HomePageURL = "https://main.dmqlib7blr1by.amplifyapp.com/";
let LobbyListOnCooldown = false;
const cooldownTime = 5000;//1000 is one second

let username = getCookie("username");
const Play = () => {
  connectWebSocket();

  return (
    <div className="App">

      <header className="App-header">
        <Card variant="outlined">
          <CardContent>
          {username = null? "loading": username}
          ^ is this your user name?
          </CardContent>

          <CardActions>
          </CardActions>

        </Card>

        <Button
          onClick={() => {
            requestPlayerList();
          }}
        >
          Request PlayerList
        </Button>
        <Button
          onClick={() => {
            disconnect();
          }}
        >
          Disconnect from backend
        </Button>
      </header>
    </div>

  );

}

const requestLobbyList = () => {
  const connectMessage =
    JSON.stringify({
      action: "lobbyList"
    });
  if (!LobbyListOnCooldown) {
    socket.send(connectMessage);
    LobbyListOnCooldown = true;
    setTimeout(() => {
      LobbyListOnCooldown = false
    }, cooldownTime);
  }
}
  const requestPlayerList = () => {
    const connectMessage =
      JSON.stringify({
        action: "listPlayers"
      });
    if (!LobbyListOnCooldown) {
      socket.send(connectMessage);
      LobbyListOnCooldown = true;
      setTimeout(() => {
        LobbyListOnCooldown = false
      }, cooldownTime);
    }

}
const disconnect = () => {
  socket.close(1000);
  window.location.replace(HomePageURL)

}

export default Play;
