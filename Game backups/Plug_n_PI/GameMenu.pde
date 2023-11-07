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
    buttons_state1[0] = new Button(GameWidth/2, 880, 400, 50, "Play Offline", GameState.Playing);
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
    text("Scan the QR code to connect, or enter the address manually:",width/2, 440);
    text(QRCodeContent, width/2, 440 + 2*fontSize);
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
    text("Hello " + connectedUserName, width/2, 440);
    text("Start a game on your phone to play!", width/2, 430+2*fontSize);
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
    textAlign(CENTER);
    textSize(fontSize);
    textLeading(fontSize);
    text(highscoreString, width/2, 420+fontSize*2);
  }
  
  // Use fill() beforehand to set text color
  void displayHighscoresCorner(String highscoreString) { 
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
