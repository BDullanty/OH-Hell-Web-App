
import {Button} from '@mui/material';
import {connectWebSocket, socket} from '../Components/Socket';

const getLobbyListMessage = 

JSON.stringify({
  route: "ListLobbyGames",
  data: {
    testData: "Hi",
    
  }
});
const Play = () =>{
  connectWebSocket();
  return (
    <div className="App">    

    <header className="App-header">
  <p>
    Click Below to send a message
  </p>
 
  <Button
  onClick={() => {
    socket.send(getLobbyListMessage);
  }}
>
  Click me
</Button>
    </header>
  </div>

  );
  
}
export default Play;
