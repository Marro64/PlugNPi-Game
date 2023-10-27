OutRunGame outRunGame;
lanedetection LaneDetection;
WebClient webClient;

int lastFrame = 0;

void setup() {
  size(920, 480, P3D);
  //LaneDetection = new lanedetection(this, 800, 400);
  outRunGame = new OutRunGame(width, height);
  webClient = new WebClient(this); 
  lastFrame = millis();
  //image(video, 0, 0 );
  frameRate(30);
}

void draw(){
  // if (LaneDetection.getlane() == "left"){
  //  outRunGame.moveLane(0);
  //}
  //if (LaneDetection.getlane() == "middle"){
  //  outRunGame.moveLane(1);
  //}
  //if (LaneDetection.getlane() == "right"){
  //  outRunGame.moveLane(2);
  //}
  
  float dt = (millis()-lastFrame)/1000.0*60;
  lastFrame = millis();
  //println(dt);
  outRunGame.update(dt);
  //LaneDetection.update();
  webClient.update();
  
  outRunGame.displayBackground();
  //LaneDetection.display();
  outRunGame.display();
  webClient.display();
}



void keyPressed() {
  if (key == 'a' || keyCode == LEFT) {
    outRunGame.moveDelta(-1);
  }
  if (key == 'd' || keyCode == RIGHT) {
    outRunGame.moveDelta(1);
  }
}

void captureEvent(Capture c) {
  c.read();
}

void moveLane(int lane) {
  outRunGame.moveLane(lane);
}
