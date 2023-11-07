import ddf.minim.*;

Minim minim;
RunnerGame RunnerGame;
lanedetection LaneDetection;
GameMenu gameMenu;
WebClient webClient;

AudioPlayer[] dopamineSound;
AudioPlayer failSound;
AudioPlayer backgroundMusic;

boolean displayLocalHighscore;
int localHighscore = 0;
String connectedUserName;

enum GameState {
  MainMenu,
  Playing,
  GameOver
}

GameState gameState;
int lastFrame = 0;

void setup() {
  //setup gamewindow
  size(1920, 980, P3D);
  
  gameState = GameState.MainMenu;

  //setup gameMenu
  gameMenu = new GameMenu(width, height);

  //setup lane Detection
  LaneDetection = new lanedetection(this, 600, 400);

  //create game
  RunnerGame = new RunnerGame(width, height);

  minim = new Minim(this);
  dopamineSound = new AudioPlayer[3];
  dopamineSound[0] = minim.loadFile("Sounds/dopamine(1).wav");
  dopamineSound[1] = minim.loadFile("Sounds/dopamine(2).wav");
  dopamineSound[2] = minim.loadFile("Sounds/dopamine(3).wav");
  failSound = minim.loadFile("Sounds/fail1.wav");
  backgroundMusic = minim.loadFile("Sounds/backgroundSong.mp3");

  //create client for server communication
  webClient = new WebClient(this);

  //load first video frame?
  lastFrame = millis();
  image(video, 0, 0 );
  frameRate(30);
}

void draw() {
  float dt = (millis()-lastFrame)/1000.0*60;
  lastFrame = millis();

  switch(gameState) {
    case MainMenu: //Main menu of the game, the game waits for a connection or offline play is pressed
      webClient.update(dt);
      
      gameMenu.display(mouseX, mouseY);
      
      switch(getOnlineState()) {
        case Connecting:
          gameMenu.displayNotConnected();
          break;
        case QRCode:
          gameMenu.displayQRCode(webClient.getQRCode(), webClient.getQRCodeContent());
          break;
        case Ready:
          gameMenu.displayPlayerConnected();
          break;
      }
    
      fill(0);
      gameMenu.displayHighscoresCorner(webClient.getHighscores());
      break;
      
    case Playing:
      RunnerGame.update(dt);
      LaneDetection.update();
      
      //display the game
      RunnerGame.displayBackground();
      RunnerGame.display();
      LaneDetection.display();
      fill(255);
      gameMenu.displayScoresCorner(RunnerGame, RunnerGame.getNewHighScore());
      gameMenu.displayHighscoresCorner(webClient.getHighscores());
      gameMenu.displayFramerate();
      gameMenu.displayQRCodeCorner(webClient.getQRCodeSmall(), webClient.getSessionID());
      break;
    
    case GameOver:
      webClient.update(dt);
      
      gameMenu.display(mouseX, mouseY);
      gameMenu.displayGameOver();
      fill(0);
      gameMenu.displayScores(RunnerGame, RunnerGame.getNewHighScore(), 2);
      gameMenu.displayHighscores(webClient.getHighscores(), 7);
      gameMenu.displayQRCodeCorner(webClient.getQRCodeSmall(), webClient.getSessionID());
      break;
  }
}

void keyPressed() {//check for keyboard inputs for keyboard controls
  if (key == 'a' || keyCode == LEFT) {
    RunnerGame.moveDelta(-1);
  }
  if (key == 'd' || keyCode == RIGHT) {
    RunnerGame.moveDelta(1);
  }
}

void mouseClicked() {
  gameMenu.MouseInput(mouseX, mouseY, gameState);
}

void captureEvent(Capture c) {//capture the camera
  if (gameState == GameState.Playing) {
    c.read();
  }
}

void startGame() {
  //webClient.updateHighscores();
  RunnerGame.reset();
  setGameState(GameState.Playing);
}

void endGame() {
  uploadScore(RunnerGame.score);
  gameState = GameState.GameOver;
}

void moveLane(int lane) {
  RunnerGame.moveDelta(lane);
}

void displayNotconnected() {
  pushMatrix();
  stroke(2);
  fill(255, 0, 0);
  ellipse(10, 10, 15, 15);
  popMatrix();
}

void playDopaminesfx() {
  for (AudioPlayer dopamine : dopamineSound) {
    dopamine.rewind();
  }
  dopamineSound[int(random(0, 3))].play();
}

void playFailsfx() {
  failSound.rewind();
  failSound.play();
}

void uploadScore(int score) {
  if(getOnlineState() == OnlineState.Ready)
  {
    webClient.uploadScore(score);
  }
  else
  {
    println("Offline play, not uploading score.");
  }
}

OnlineState getOnlineState() {
  return webClient.onlineState;
}

boolean isOnline() {
  return webClient.onlineState == OnlineState.Ready;
}

GameState getGameState() {
  return gameState;
}

void setGameState(GameState newGameState) {
  gameState = newGameState;
}
