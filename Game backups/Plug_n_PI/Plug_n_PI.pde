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
  
  if (gameState == GameState.MainMenu || gameState == GameState.GameOver) {
    gameMenu.display(mouseX, mouseY);
    gameMenu.displayHighscores(webClient.getHighscores(), 0);
  }

  if (gameState == GameState.MainMenu) {//Main menu of the game, the game waits for a connection or offline play is pressed
    //display logo and menu select 'offline play or connect'
    //update connection ore sth
    webClient.update(dt);
    
    
    switch(getOnlineState()) {
      case Connecting:
        gameMenu.displayNotConnected();
        break;
      case QRCode:
        gameMenu.displayQRCode(webClient.getQRCode(), webClient.getQRCodeContent());
        break;
      case Ready:
        gameMenu.displayPlayerConnected();
    }
    
    if (getOnlineState() == OnlineState.Ready) {//game logged in
      
    }
  }

  if (gameState == GameState.Playing) {//run the game
    //get delta time and update game
    update(dt);

    //display the game
    RunnerGame.displayBackground();
    RunnerGame.display(LaneDetection.passvideo());
    LaneDetection.display();
    gameMenu.displayHighscores(webClient.getHighscores(), 255);
  }

  if (gameState == GameState.GameOver) {//reset game and return Highscore
    gameMenu.displayGameOver();
  }
}

void update(float dtime) {
  RunnerGame.update(dtime);
  LaneDetection.update();
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
  webClient.updateHighscores();
  RunnerGame.reset();
  setGameState(GameState.Playing);
}

void moveLane(int lane) {
  RunnerGame.moveLane(lane);
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
  return webClient.onlineState == OnlineState.Ready || webClient.onlineState == OnlineState.Uploading;
}

GameState getGameState() {
  return gameState;
}

void setGameState(GameState newGameState) {
  gameState = newGameState;
}
