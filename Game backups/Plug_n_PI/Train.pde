class Train {

  float distMoved = 0;
  float startY = -300;
  float posX;
  float posY;
  boolean hasPassed = false;
  float wScale, hScale;
  PImage trainFront;
  PImage trainSide;
  PImage trainTop;
  float gScale = 1;

  Train(PImage TrainFront, PImage TrainSide, PImage TrainTop, float PosX, float PosY) {
    posX = PosX;
    posY = PosY;
    trainFront =TrainFront;
    trainSide = TrainSide;
    trainTop = TrainTop;
  }

  void update(float dt, float speed) {
    distMoved -= speed*dt;
    posY = startY-distMoved;
  }

  void display() {
    stroke(255);
    fill(0, 70, 25);
    pushMatrix();
    noStroke();
    translate(posX, posY*gScale, 32);
    scale(32);
    beginShape(QUADS);
    texture(trainFront);
    //front
    vertex(-0.625, 1, 1, 0, 0);
    vertex( 0.625, 1, 1, 1, 0);
    vertex( 0.625, 1, -1, 1, 1);
    vertex(-0.625, 1, -1, 0, 1);
    endShape();
    beginShape(QUADS);
    texture(trainSide);
    //rightside
    vertex( 0.625, -1, 1, 0, 0);
    vertex( 0.625, -1, -1, 1, 0);
    vertex( 0.625, 1, -1, 1, 1);
    vertex( 0.625, 1, 1, 0, 1);
    endShape();
    beginShape(QUADS);
    texture(trainSide);
    scale(1, 1, -1);
    //leftside
    vertex(-0.625, -1, -1, 0, 0);
    vertex(-0.625, -1, 1, 1, 0);
    vertex(-0.625, 1, 1, 1, 1);
    vertex(-0.625, 1, -1, 0, 1);
    endShape();
    scale(1, 1, -1);

    beginShape(QUADS);
    texture(trainTop);
    //top
    vertex(-0.625, -1, 1, 0, 0);
    vertex( 0.625, -1, 1, 1, 0);
    vertex( 0.625, 1, 1, 1, 1);
    vertex(-0.625, 1, 1, 0, 1);
    endShape();


    popMatrix();
  }

  float[] getPosition() {
    float[] vec = {posX, posY};
    return vec;
  }
  
  boolean collideWith(float otherX, float otherY){
    if(otherY > posY && otherY < posY + 32 && posX == otherX){
      return true;
    }
    return false;
  }
  
  void reset(){
    distMoved = 10000;
  }
  
  void rescale(float scale){
    gScale = scale;
  }
}
