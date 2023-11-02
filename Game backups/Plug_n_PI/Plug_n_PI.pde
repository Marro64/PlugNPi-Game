import ddf.minim.*;

Minim minim;
RunnerGame RunnerGame;
lanedetection LaneDetection;
GameMenu gameMenu;
WebClient webClient;

AudioPlayer[] dopamineSound;
AudioPlayer failSound;
AudioPlayer backgroundMusic;

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

  if (gameState == 0) {//Main menu of the game, the game waits for a connection or offline play is pressed
    //display logo and menu select 'offline play or connect'
    gameMenu.display(mouseX, mouseY);
    //update connection ore sth
    webClient.update();
    //display QR code
    webClient.display();

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

    if (isConnected) {
      gameState = 99;
    }
  }

  if (gameState == 2) {// make connection
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
