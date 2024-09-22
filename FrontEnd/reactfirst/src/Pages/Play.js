import React, { useState, useEffect } from 'react';

import { Button } from '@mui/material';
import { connectWebSocket, socket } from '../Components/Socket';
import { getCookie } from '../Components/cookies';
import { PlayerList } from '../Components/playerlist';
import { GameList } from '../Components/lobbylist';
const HomePageURL = "https://main.dmqlib7blr1by.amplifyapp.com/";
let LobbyListOnCooldown = false;
const cooldownTime = 5000;//1000 is one second
const Play = () => {
  const [users, setUsers] = useState({});
  const [games, setGames] = useState({});
  useEffect(() => {
    connectWebSocket(setUsers,setGames); // Pass state setters directly
  }, []);

  return (
    <div className="App">

      <header className="App-header">
      <h1>Welcome</h1>
      <PlayerList users={users} className="userList"/>
      <Button
          onClick={() => {
            createGame();
          }}
        >
          Create a game
        </Button>
      <GameList gameList={games} className="lobbyList"/>

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

const createGame = () => {
  const createGameMessage =
    JSON.stringify(
      {
      action: "createGame"
    }
  );
  console.log(createGameMessage)
  if (!LobbyListOnCooldown) {
    socket.send(createGameMessage);
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
