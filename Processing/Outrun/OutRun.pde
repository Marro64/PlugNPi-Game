OutRunGame outRunGame;

int lastFrame = 0;

void setup() {
  size(1920, 1080, P3D);
  frameRate(60);
  
  outRunGame = new OutRunGame(width, height);
  lastFrame = millis();
}

void draw(){
  float dt = (millis()-lastFrame)/1000.0*60;
  lastFrame = millis();
  println(dt);
  outRunGame.update(dt);
  outRunGame.display();
}



void keyPressed() {
  if (key == 'a' || keyCode == LEFT) {
    outRunGame.moveDelta(-1);
  }
  if (key == 'd' || keyCode == RIGHT) {
    outRunGame.moveDelta(1);
  }
}
