class GameMenu {
  PImage titleImage;
  Button[] buttons_state1;
  int fontSize = 30;
  int textBaseOffset = 430;

  GameMenu() {
    titleImage = loadImage("PlugNPI.png");
    buttons_state1 = new Button[1];
    buttons_state1[0] = new Button(width/2, height-200, 400, 50, "Play Offline", GameState.Playing);
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
    textSize(fontSize);
    text("Not connected", width/2, 440);
    text("Press the button below to play offline.", width/2, 440+2*fontSize);
  }
  
  void displayQRCode(PImage QRCode, String QRCodeContent) {
    imageMode(CENTER);
    image(QRCode, width/2, 550 + 4*fontSize);
    textSize(fontSize);
    textAlign(CENTER);
    text("Scan the QR code to connect, or enter the address manually:",width/2, textBaseOffset);
    text(QRCodeContent, width/2, textBaseOffset + 2*fontSize);
  }
  
  void displayQRCodeCorner(PImage QRCode, String QRCodeContent) {
    if(QRCode != null) {
      imageMode(CORNER);
      image(QRCode, width-QRCode.width, height-QRCode.height-2*fontSize);
      textSize(fontSize);
      textAlign(RIGHT);
      hint(DISABLE_DEPTH_TEST);
      text(QRCodeContent, width-20, height-fontSize);
      hint(ENABLE_DEPTH_TEST);
    }
  }

  void displayPlayerConnected() {
    textAlign(CENTER);
    fill(0);
    textSize(fontSize);
    text("Hello " + connectedUserName, width/2, textBaseOffset);
    text("Start a game on your phone to play!", width/2, textBaseOffset+2*fontSize);
  }
  
  void displayGameOver() {
    textAlign(CENTER);
    fill(0);
    textSize(fontSize);
    text("Game Over!", width/2, textBaseOffset);
  }

  void displayTitleImage() {
    pushMatrix();
    imageMode(CENTER);
    translate(width/2, 0);
    scale(0.5);
    image(titleImage, 0, 400);
    popMatrix();
  }
  
  // Use fill() beforehand to set text color
  void displayHighscores(String highscoreString, int linesOffset) { 
    textAlign(CENTER);
    textSize(fontSize);
    textLeading(fontSize);
    text(highscoreString, width/2, textBaseOffset+fontSize*linesOffset);
  }
  
  // Use fill() beforehand to set text color
  void displayHighscoresCorner(String highscoreString) { 
    textAlign(RIGHT);
    textSize(fontSize);
    textLeading(fontSize);
    text(highscoreString, width-20, fontSize);
  }
  
  // Use fill() beforehand to set text color
  void displayScores(RunnerGame game, boolean newHighScore, int linesOffset) {
    textSize(fontSize);
    textAlign(CENTER);
    
    text(webClient.username, width/2, textBaseOffset+fontSize*linesOffset);
    text("Score: " + game.score, width/2, textBaseOffset+fontSize*(linesOffset+1));
    text("Coins: " +((int)game.colScore) + "/50", width/2, textBaseOffset+fontSize*(linesOffset+2));
    if(newHighScore) {
      text("New Highscore!", width/2, textBaseOffset+fontSize*(linesOffset+3));
    } else {
      text("Highscore: " + game.gameHighScore, width/2, textBaseOffset+fontSize*(linesOffset+3));
    }
  }
  
  // Use fill() beforehand to set text color
  void displayScoresCorner(RunnerGame game, boolean newHighScore) {
    textSize(fontSize);
    textAlign(LEFT);
    
    text(webClient.username, 20, fontSize);
    text("Score: " + game.score, 20, fontSize*2);
    text("Coins: " +((int)game.colScore) + "/50", 20, fontSize*3);
    if (game.lives >= 0) text("lives: " + game.lives, 20, fontSize*4);
    if (game.gameHighScore > 0) {
      if(newHighScore) {
        text("New Highscore!", 20, fontSize*5);
      } else {
        text("Highscore: " + game.gameHighScore, 20, fontSize*5);
      }
    }
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
