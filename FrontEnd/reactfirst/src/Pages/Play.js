
import {Button} from '@mui/material';
import {connectWebSocket, socket} from '../Components/Socket';
import { getCookie } from '../Components/cookies';

let LobbyListOnCooldown= false;
const cooldownTime = 5000;//1000 is one second

let connectMessage;
let disconnectMessage;
const Play = () =>{
  connectWebSocket();
 
  return (
    <div className="App">    

    <header className="App-header">

 
  <Button
  onClick={() => {

    connectOnline();
    
  }}
>
  Connect to backend
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

  if(!LobbyListOnCooldown){
  console.log("Request Json: ",connectMessage);
  socket.send(connectMessage);
  LobbyListOnCooldown=true;
  setTimeout(()=>{
      LobbyListOnCooldown=false
    }, cooldownTime);
  }
  //We can set the disconnect message now

}
const disconnect = () =>{
  disconnectMessage = 
  JSON.stringify({
    action: "disconnect",
    sub: `${getCookie('sub')}`  
  });
  
  if(!LobbyListOnCooldown){
    console.log("Disconnected!");
    socket.close(200,disconnectMessage);
    LobbyListOnCooldown=true;
    setTimeout(()=>{
        LobbyListOnCooldown=false
      }, cooldownTime);
    }
}
export default Play;
