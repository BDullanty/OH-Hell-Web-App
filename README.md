# OH-Hell-Web-App
A Multiplayer Web Based card game hosted with AWS Amplify, AWS Websockets, AWS Cognito, AWS Lambda, React, JavaScript, HTML, CSS, and Java.


So:

We will have AWS Dynamo Tables.

We will have AWS Lambda Java Api calls that will check who is calling it, check the database, and send the appropriate action.

Web sockets will be a middle man for creating a connection and passing the user token along.

Amplify will display in game buttons that will send the requests to our web sockets, such as create game, play card, ect.
