import React from 'react';
import {Paper,List, ListItem, ListItemText, Typography } from '@mui/material';
export const PlayerList = ({ users }) => {
  return (
    <div>
     <Paper elevation={2} sx={{  }}>
      <Typography variant="h6">Player List:</Typography>
      <List>
        {Object.entries(users).map(([username, status]) => (
          <ListItem key={username}>
            <ListItemText primary={username} secondary={status} />
          </ListItem>
        ))}
      </List>
      </Paper>
    </div>
  );
};


