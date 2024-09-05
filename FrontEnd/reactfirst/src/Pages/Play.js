
import {Button} from '@mui/material';
import {connectWebSocket, socket} from '../Components/Socket';

const getLobbyListMessage = 

JSON.stringify({
  action: "ListLobbyGames",
  data: {
    testData: "Hi",
    
  }
});
const Play = () =>{
  connectWebSocket();
  return (
    <div className="App">    

    <header className="App-header">

 
  <Button
  onClick={() => {
    console.log(getLobbyListMessage);
    socket.send(getLobbyListMessage);
  }}
>
  Refresh lobbyList
</Button>
    </header>
  </div>

  );
  
}
export default Play;
