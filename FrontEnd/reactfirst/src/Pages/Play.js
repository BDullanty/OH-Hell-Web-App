
import {Button} from '@mui/material';
import {connectWebSocket, socket} from '../Components/Socket';

let LobbyListOnCooldown= false;
const cooldownTime = 5000;

const getLobbyListMessage = 

{
  "action": "ListLobbyGames",
  "data": {
    "testData": "Hi"
    
  }
};
const Play = () =>{
  connectWebSocket();
  
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
