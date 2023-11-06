class GameMenu {
  PImage titleImage;
  int gameWidth;
  int gameHeight;
  Button[] buttons_state1;
  int fontSize = 30;

  GameMenu(int GameWidth, int GameHeight) {
    gameWidth = GameWidth;
    gameHeight = GameHeight;
    titleImage = loadImage("PlugNPI.png");
    buttons_state1 = new Button[1];
    buttons_state1[0] = new Button(GameWidth/2, 800, 400, 50, "Play Offline", GameState.Playing);
    //buttons_state1[1] = new Button(GameWidth/2 + 110, 420, 200, 50, "Play Online", 2);
  }

  void display(int mX, int mY) {
    background(255);
    displayTitleImage();
    for (Button button : buttons_state1) {
      button.display(mX, mY);
    }
  }
  
  void displayNotConnected() {
    textAlign(CENTER);
    fill(0);
    textSize(20);
    text("Not connected", width/2, 420);
    text("Press the button below to play offline.", width/2, 440);
  }
  
  void displayQRCode(PImage QRCode, String QRCodeContent) {
    pushMatrix();
    translate(width/2, 520 + 2*fontSize);
    scale(3);
    imageMode(CENTER);
    image(QRCode, 0, 0);
    popMatrix();
    textSize(fontSize);
    textAlign(CENTER);
    text("Scan the QR code to connect, or enter the address manually:",width/2, 410);
    text(QRCodeContent, width/2, 410 + fontSize);
  }

  void displayPlayerConnected() {
    textAlign(CENTER);
    fill(0);
    textSize(fontSize);
    text("Hello " + connectedUserName, width/2, 420);
    text("Start a game on you phone to play!", width/2, 440+fontSize);
  }
  
  void displayGameOver() {
    textAlign(CENTER);
    fill(0);
    textSize(fontSize);
    text("Game Over!", width/2, 420);
  }

  void displayTitleImage() {
    pushMatrix();
    imageMode(CENTER);
    translate(gameWidth/2, 0);
    scale(0.5);
    image(titleImage, 0, 400);
    popMatrix();
  }
  
  // Use fill() beforehand to set text color
  void displayHighscores(String highscoreString) { 
    textAlign(RIGHT);
    textSize(fontSize);
    textLeading(fontSize);
    text(highscoreString, width-20, fontSize);
  }
  
  // Use fill() beforehand to set text color
  void displayScores(RunnerGame game) {
    textSize(fontSize);
    textAlign(LEFT);
    
    text("Score: " + game.score, 20, fontSize);
    text("Coins: " +((int)game.colScore) + "/100", 20, fontSize*2);
    if (game.lives >= 0) text("lives: " + game.lives, 20, fontSize*3);
    if (game.gameHighScore > 0) text("Highscore: " + game.gameHighScore, 20, fontSize*4);
  }
  
  void displayFramerate() {
    textSize(fontSize);
    textAlign(LEFT);
    text("FPS: " + frameRate, 20, height-20);
  }

  void MouseInput(int Mx, int My, GameState gameState) {
    if (gameState == GameState.MainMenu || gameState == GameState.GameOver) {
      for (Button button : buttons_state1) {
        if (button.isInbox(Mx, My)) {
          gameState = button.toGameState;
          if (button.toGameState == GameState.Playing) {
            startGame();
          }
        }
      }
    } else if (gameState == GameState.Playing) {
      gameState = GameState.MainMenu;
    }
  }
}


//class GameMenu {
//  PImage titleImage;
//  int gameWidth;
//  int gameHeight;
//  Button[] buttons_state1;

//  GameMenu(int GameWidth, int GameHeight) {
//    gameWidth = GameWidth;
//    gameHeight = GameHeight;
//    titleImage = loadImage("PlugNPI.png");
//    buttons_state1 = new Button[1];
//    buttons_state1[0] = new Button(GameWidth/2, 700, 200, 50, "Play Offline", 1);
//    //buttons_state1[1] = new Button(GameWidth/2 + 110, 420, 200, 50, "Play Online", 2);
//  }

//  void display(int mX, int mY) {
//    background(255);
//    displayTitleImage();
//    for (Button button : buttons_state1) {
//      button.display(mX, mY);
//    }
//  }

//  void displayReturningHighscore() {
//    background(255);
//    displayTitleImage();
//    textAlign(CENTER);
//    text("Returning highscore, please wait...", width/2, 420);
//  }

//  void displayPlayerConnected(int mX, int mY) {
//    background(255);
//    displayTitleImage();
//    textAlign(CENTER);
//    fill(0);
//    textSize(20);
//    text("Hello " + connectedUserName, width/2, 420);
//    text("Start a game on you phone to play!", width/2, 440);
//    for (Button button : buttons_state1) {
//      button.display(mX, mY);
//    }
//  }

//  void displayTitleImage() {
//    pushMatrix();
//    imageMode(CENTER);
//    translate(gameWidth/2, 0);
//    scale(0.5);
//    image(titleImage, 0, 400);
//    popMatrix();
//  }

//  void MouseInput(int Mx, int My, int gamestate) {
//    if (gamestate == 0 || gamestate == 2) {
//      for (Button button : buttons_state1) {
//        if (button.isInbox(Mx, My)) {
//          gameState = button.toGameState;
//          if (button.toGameState == GameState.Playing) {
//            RunnerGame.reset();
//            setGameState(GameState.Playing);
//          }
//        }
//      }
//    } else if (gamestate == 1) {
//      gameState = 0;
//    }
//  }
//}
