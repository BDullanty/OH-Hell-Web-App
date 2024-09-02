
import {Button} from '@mui/material';
import {connectWebSocket} from '../Components/Socket';
const Counter = () =>{
 
  return (
    <div className="App">    

    <header className="App-header">
  <p>
  Click Below to create a websocket
    </p>
 
  <Button
  onClick={() => {
    connectWebSocket();
  }}
>
  Click me
</Button>
    </header>
  </div>

  );
  
}
export default Counter;
