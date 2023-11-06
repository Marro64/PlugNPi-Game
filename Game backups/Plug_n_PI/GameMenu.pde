class GameMenu {
  PImage titleImage;
  int gameWidth;
  int gameHeight;
  Button[] buttons_state1;

  GameMenu(int GameWidth, int GameHeight) {
    gameWidth = GameWidth;
    gameHeight = GameHeight;
    titleImage = loadImage("PlugNPI.png");
    buttons_state1 = new Button[1];
    buttons_state1[0] = new Button(GameWidth/2, 700, 200, 50, "Play Offline", GameState.Playing);
    //buttons_state1[1] = new Button(GameWidth/2 + 110, 420, 200, 50, "Play Online", 2);
  }

  void display(int mX, int mY) {
    background(255);
    displayTitleImage();
    for (Button button : buttons_state1) {
      button.display(mX, mY);
    }
  }

  void displayReturningHighscore() {
    background(255);
    displayTitleImage();
    textAlign(CENTER);
    text("Returning highscore, please wait...", width/2, 420);
  }

  void displayPlayerConnected(int mX, int mY) {
    background(255);
    displayTitleImage();
    textAlign(CENTER);
    fill(0);
    textSize(20);
    text("Hello " + connectedUserName, width/2, 420);
    text("Start a game on you phone to play!", width/2, 440);
    for (Button button : buttons_state1) {
      button.display(mX, mY);
    }
  }

  void displayTitleImage() {
    pushMatrix();
    imageMode(CENTER);
    translate(gameWidth/2, 0);
    scale(0.5);
    image(titleImage, 0, 400);
    popMatrix();
  }

  void MouseInput(int Mx, int My, GameState gameState) {
    if (gameState == GameState.MainMenu || gameState == GameState.GameOver) {
      for (Button button : buttons_state1) {
        if (button.isInbox(Mx, My)) {
          gameState = button.toGameState;
          if (button.toGameState == GameState.Playing) {
            println("Starting offline game.");
            RunnerGame.reset();
            gameState = GameState.Playing;
          }
        }
      }
    } else if (gameState == GameState.Playing) {
      gameState = GameState.MainMenu;
    }
  }
}
