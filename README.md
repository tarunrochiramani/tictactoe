# tictactoe

Welcome to TR's Tic Tac Toe for Slack. 
By default the traditional 3 * 3 board begins.

The following slash commands have been created

* /ttt @user --> This will request a Game challenge to the mentioned user
* /ttt-move [row][col] --> Play a move. eg /ttt-move 0 2 
* /ttt-current --> Shows the current game going on in the channel where the command is executed
* /ttt-abort --> Allows the user (whose turn it is) to abort the game.


# Flow of Game
1. A user in any channel can challenge another user in the same channel. The challenge message is visible to all in the given channel.
2. The challenged user gets a message to either "Accept" or "Reject" the challenge. This message is only visible to the second user. 
3. On Rejecting the challenge, a message is shown in the given challenge about the response.
4. On Accepting the challenge, the game begins and the board is visibile to all the in channel. 
5. The game currently by default assigns 'X' to the challenged user and lets this user begin the game.
6. The game also shows which player is assigned a 'X' or 'O' and whose turn it is, on the board.
7. Once the game is over, the winner is displayed.

# Edge cases
1. Multiple game request on the same channel can be sent. 
2. Only once any challenged user accepts the challenge, a game begins in the current channel. A user in a challenge could recieve multiple challenges, but its upto this user to choose which challenge to accept. On accepting one challenge, currently all other challenge messages are not removed. A slight nice to have. 
3. ONLY ONE game per channel is permitted. 
4. While a game is going on, a new game request/challenge is not allowed. 
5. Only the user's whose turn it is, is allowed to play the move OR can abort the game.


# Design
The current app is based on
1. Spring Boot
2. Java 
3. REST API's

The app is hosted on Google App Engine.

# Nice to have's

1. Current app is a complete in-memory application. A Database could be connected behind. The type of DB could be based on the following needs 
  (a) Number of users this game is distributed to?
  (b) Audit needs of every move per channel / user.
  (c) Allowing the size of the board to be decided by the users requesting the game. 
2. Based on the regions this app is distributed, i18N could be adapted.
3. Most important of all, the visual representation of the Game could be enhanced.
4. Multiple games in a channel could be allowed, however a decision about how many games to allow is important (since all 'n' game boards will be visible in the channel, which could degrade user experience). 
5. OAuth code implementation to gain "Access Token".
