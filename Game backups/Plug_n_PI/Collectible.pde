class Collectible {
  float distMoved = 0;
  PImage collectibleImage;
  float scale = 64;
  float startY;
  float posX;
  float posY;
  boolean hasPassed = false;
  float rotation = 0;


  Collectible(PImage colImg, float PosX, float PosY) {
    posX = PosX;
    posY = PosY;
    startY = posY;
    collectibleImage = colImg;
  }

  void update(float speed, float dt) {
    distMoved -= speed*dt;
    posY += speed;
    rotation += speed*0.5;
  }

  void display() {
    pushMatrix();
    beginShape(QUADS);
    texture(collectibleImage);
    textureMode(NORMAL);
    //front
    translate(posX, posY, 30);
    scale(24,32,32);
    scale(0.3);
    rotate(rotation*0.1);
    vertex(-1, 0, 1, 0, 0);
    vertex( 1, 0, 1, 1, 0);
    vertex( 1, 0, -1, 1, 1);
    vertex(-1, 0, -1, 0, 1);
    endShape();
    popMatrix();
  }

  boolean collideWith(float otherX, float otherY) {
    if (otherY > posY-64 && otherY < posY && posX == otherX) {
      return true;
    }
    return false;
  }
  
  void reset(){
    posY = 10000;
  }
}
