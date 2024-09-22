import React from 'react';
import { Button, Paper, List, ListItem, ListItemText, Typography } from '@mui/material';
import { getCookie } from './cookies';
import { voteStart } from './Socket';

export const GameList = ({ gameList }) => {
  {

    return (
      <div>
        <Paper elevation={2} sx={{}}>
          <Typography variant="h6">Game List:</Typography>
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
                {getCookie('gameID') === gameID &&
                gameDetails.state === "WAITING" && (
                  <div>
                    <Button onClick={() => {
                      voteStart();
                    }}>Vote to start</Button>
                  </div>
                )}
                {getCookie('gameID') !== gameID &&
                gameDetails.state === "WAITING" &&(
                  <div>
                    <Button onClick={() => {

                    }}
                    >Join Game</Button>
                  </div>
                )}
                {gameDetails.state === "INGAME" && (
                  <Typography variant="body2">In game, Round: {gameDetails.round}</Typography>
                )}

              </ListItem>
            ))}
          </List>
        </Paper>
      </div>
    );
  };
}