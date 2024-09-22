import React from 'react';
import { Paper, List, ListItem, ListItemText, Typography } from '@mui/material';
import { getCookie } from './cookies';
export const GameList = ({ gameList }) => {
  {

    return (
      <div>
        <Paper elevation={2} sx={{}}>
          <Typography variant="h6">Lobby List:</Typography>
          <List>
            {Object.entries(gameList).map(([gameID, gameDetails]) => (
              <ListItem key={gameID}>
                <ListItemText primary={`${gameDetails.host}'s game`} secondary={gameDetails.state} />
                
                <List>
                  {[gameDetails.host, gameDetails.player2, gameDetails.player3, gameDetails.player4, gameDetails.player5].map((player, index) => (
                    player && (
                      <ListItem key={index}>
                        <ListItemText primary={player} />
                      </ListItem>
                    )
                  ))}
                </List>
                {getCookie('gameID') ===  gameDetails.gameID && (
                <Typography variant="body2">Vote to start?</Typography>
              )}
                {gameDetails.state === "INGAME" && (
                <Typography variant="body2">In game, Round: {gameDetails.round}</Typography>
              )}
              {gameDetails.state === "LOBBY" && (
                <Typography variant="body2">Lobby {gameDetails.round}</Typography>
              )}

              </ListItem>
            ))}
          </List>
        </Paper>
      </div>
    );
  };

}