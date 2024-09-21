import React from 'react';
import { List, ListItem, ListItemText, Typography } from '@mui/material';
import {socket} from './Socket';
export const PlayerList = ({ users }) => {
  return (
    <div>
      <Typography variant="h6">Player List</Typography>
      <List>
        {Object.entries(users).map(([username, status]) => (
          <ListItem key={username}>
            <ListItemText primary={username} secondary={status} />
          </ListItem>
        ))}
      </List>
    </div>
  );
};
export const requestPlayerList = () => {
  const connectMessage =
    JSON.stringify({
      action: "listPlayers"
    });
    socket.send(connectMessage);
}

