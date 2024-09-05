
import {Button} from '@mui/material';
import {connectWebSocket} from '../Components/Socket';
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
    //send message to lambda function
  }}
>
  Click me
</Button>
    </header>
  </div>

  );
  
}
export default Play;
