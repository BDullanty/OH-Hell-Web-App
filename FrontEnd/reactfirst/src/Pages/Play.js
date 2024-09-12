
import {Button} from '@mui/material';
import {connectWebSocket, socket} from '../Components/Socket';
import { getCookie } from '../Components/cookies';

let LobbyListOnCooldown= false;
const cooldownTime = 5000;//1000 is one second


const Play = () =>{
  connectWebSocket();
  const connectMessage = 

JSON.stringify({
  action: "connect",
  jwk: `${getCookie('access_token')}`  
});

  return (
    <div className="App">    

    <header className="App-header">

 
  <Button
  onClick={() => {

   
    if(!LobbyListOnCooldown){
      console.log("Request Json: ",connectMessage);
      socket.send(connectMessage);
      LobbyListOnCooldown=true;
      setTimeout(()=>{
          LobbyListOnCooldown=false
        }, cooldownTime);
      }
    
  }}
>
  Connect to backend
</Button>
    </header>
  </div>

  );
  
}
export default Play;
