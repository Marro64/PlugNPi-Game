class GameMenu {
  PImage titleImage;
  int gameWidth;
  int gameHeight;
  Button[] buttons_state1;

  GameMenu(int GameWidth, int GameHeight) {
    gameWidth = GameWidth;
    gameHeight = GameHeight;
    titleImage = loadImage("PlugNPI.png");
    buttons_state1 = new Button[2];
    buttons_state1[0] = new Button(GameWidth/2 - 110, 420, 200, 50, "Play Offline", 1);
    buttons_state1[1] = new Button(GameWidth/2 + 110, 420, 200, 50, "Play Online", 2);
  }

  void display(int mX, int mY) {
    background(255);
    pushMatrix();
    imageMode(CENTER);
    translate(gameWidth/2, 0);
    scale(0.5);
    image(titleImage, 0, 400);
    popMatrix();
    for (Button button : buttons_state1) {
      button.display(mX, mY);
    }
  }

  void MouseInput(int Mx, int My, int gamestate) {
    if (gamestate == 0) {
      for (Button button : buttons_state1) {
        if (button.isInbox(Mx, My)) {
          gameState = button.toGameState;
        }
      }
    }
    else if(gamestate == 1){
      gameState = 0;
    }
  }
}
