
import {Button} from '@mui/material';
import {connectWebSocket, socket} from '../Components/Socket';
import { getCookie } from '../Components/cookies';

let LobbyListOnCooldown= false;
const cooldownTime = 5000;//1000 is one second


const Play = () =>{
  connectWebSocket();
  const getLobbyListMessage = 

JSON.stringify({
  action: "ListLobbyGames",
  token: `${getCookie('access_token')}`
    
  
});
  return (
    <div className="App">    

    <header className="App-header">

 
  <Button
  onClick={() => {

   
    if(!LobbyListOnCooldown){
      console.log("Request Json: ",getLobbyListMessage);
      socket.send(getLobbyListMessage);
      LobbyListOnCooldown=true;
      setTimeout(()=>{
          LobbyListOnCooldown=false
        }, cooldownTime);
      }
    
  }}
>
  Refresh lobbyList
</Button>
    </header>
  </div>

  );
  
}
export default Play;
