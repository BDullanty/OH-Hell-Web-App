import React, { useState, useEffect } from 'react';

import {  Button } from '@mui/material';
import { connectWebSocket, socket } from '../Components/Socket';
import { getCookie } from '../Components/cookies';
import PlayerList from '../Components/playerlist';
const HomePageURL = "https://main.dmqlib7blr1by.amplifyapp.com/";
let LobbyListOnCooldown = false;
const cooldownTime = 5000;//1000 is one second

let username = getCookie("username");
const Play = () => {
  const [users, setUsers] = useState({});

  useEffect(() => {
    connectWebSocket(setUsers); // Pass state setters directly
  }, []);
  return (
    <div className="App">

      <header className="App-header">
      <h1>Play Page</h1>
      <PlayerList users={users} />


        <Button
          onClick={() => {
            requestPlayerList();
          }}
        >
          Request PlayerList
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

const requestLobbyList = () => {
  const connectMessage =
    JSON.stringify({
      action: "lobbyList"
    });
  if (!LobbyListOnCooldown) {
    socket.send(connectMessage);
    LobbyListOnCooldown = true;
    setTimeout(() => {
      LobbyListOnCooldown = false
    }, cooldownTime);
  }
}
  const requestPlayerList = () => {
    const connectMessage =
      JSON.stringify({
        action: "listPlayers"
      });
    if (!LobbyListOnCooldown) {
      socket.send(connectMessage);
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
