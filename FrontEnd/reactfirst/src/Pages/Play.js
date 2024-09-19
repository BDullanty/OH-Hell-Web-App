
import { Box, Button } from '@mui/material';
import { connectWebSocket, socket } from '../Components/Socket';
import { getCookie } from '../Components/cookies';
const HomePageURL = "https://main.dmqlib7blr1by.amplifyapp.com/";
let LobbyListOnCooldown = false;
const cooldownTime = 5000;//1000 is one second

let username = getCookie("username");
const Play = () => {
  connectWebSocket();

  return (
    <div className="App">

      <header className="App-header">
        <Box>
        {username = null? "loading": username}
        </Box>

        <Button
          onClick={() => {
            requestLobbyList();
          }}
        >
          Refresh Lobbies
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
    connectWebSocket();
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
