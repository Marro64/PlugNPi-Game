RunnerGame RunnerGame;
lanedetection LaneDetection;
GameMenu gameMenu;
WebClient webClient;

boolean isConnected;
boolean displayLocalHighscore;
int localHighscore = 0;

int gameState = 0;
int lastFrame = 0;

void setup() {
  isConnected = false;

  //setup gamewindow
  size(1920, 980, P3D);

  //setup gameMenu
  gameMenu = new GameMenu(width, height);

  //setup lane Detection
  LaneDetection = new lanedetection(this, 600, 400);

  //create game
  RunnerGame = new RunnerGame(width, height);
  
  //create client for server communication
  webClient = new WebClient(this);

  //load first video frame?
  lastFrame = millis();
  image(video, 0, 0 );
  frameRate(30);
}

void draw() {

  if (gameState == 0) {//run the game menu
    //display logo and menu select 'offline play or connect'
    gameMenu.display(mouseX, mouseY);

    if (!isConnected) {//display if game is connected or not
      displayNotconnected();
    }
  }

  if (gameState == 1) {//run the game
    //get delta time and update game
    float dt = (millis()-lastFrame)/1000.0*60;
    lastFrame = millis();
    update(dt);

    //display the game
    RunnerGame.displayBackground();
    RunnerGame.display(LaneDetection.passvideo());
    LaneDetection.display();
  }

  if (gameState == 2) {// make connection
    //update connection ore sth
    webClient.update();
    //display QR code
    webClient.display();
  }

  if (gameState == 99) {//reset game and return Highscore
    //int endScore = Pigame.giveScore();
    //if (endScore > localHighscore){
    //  displayLocalHighscore = true;
    //  localHighscore = endScore
    //}
    //Pigame.reset();
    //display Highscore
    //display waiting screen
    //ifConnected:
    //  return highscore and wait until highscore is returned
    //  return to connected mode/waiting for input mode
    //else{
    //  gameState = 0;
    //}
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
  if (gameState == 1) {
    c.read();
  }
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
