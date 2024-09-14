
import { Button } from '@mui/material';
import { connectWebSocket, socket } from '../Components/Socket';
import { getCookie } from '../Components/cookies';
const HomePageURL = "https://main.dmqlib7blr1by.amplifyapp.com/";
let LobbyListOnCooldown = false;
const cooldownTime = 5000;//1000 is one second

let connectMessage;
let disconnectMessage;
const Play = () => {
  connectWebSocket();

  return (
    <div className="App">

      <header className="App-header">


        <Button
          onClick={() => {

            connectOnline();

          }}
        >
          reconnect
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

const connectOnline = () => {
  connectMessage =
    JSON.stringify({
      action: "connect",
      jwk: `${getCookie('access_token')}`
    });

  if (!LobbyListOnCooldown) {
    console.log("Request Json: ", connectMessage);
    socket.send(connectMessage);
    LobbyListOnCooldown = true;
    setTimeout(() => {
      LobbyListOnCooldown = false
    }, cooldownTime);
  }


}
const disconnect = () => {
  socket.close(1000);
  console.log("Disconnected!");


  LobbyListOnCooldown = true;
  setTimeout(() => {
    LobbyListOnCooldown = false
  }, cooldownTime);

  //window.location.replace(HomePageURL)

}
export default Play;
