# OH-Hell-Web-App
A Multiplayer Web Based card game hosted with AWS Amplify, AWS Websockets, AWS Cognito, AWS Lambda, React, JavaScript, HTML, CSS, and Java.

Architecture Diagram:
![image](https://github.com/user-attachments/assets/f53b9fa9-d553-4100-b21a-804cc2cde743)

We will have AWS Dynamo Tables that represent the state of the games.


We will have AWS Lambda Java Api calls that on trigger will check the database, and send the appropriate action to all players connected to that game.

Web sockets will be used for creating a connection and taking input such as play card, bet, and join game.

Amplify will display in game buttons that will send the requests to our web sockets, such as create game, play card, ect, which will send requests via our websocket
