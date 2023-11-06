class Collectible {
  float distMoved = 0;
  PImage collectibleImage;
  float scale = 64;
  float posX;
  float posY;
  boolean hasPassed = false;
  float rotation = 0;


  Collectible(PImage colImg, float PosX, float PosY) {
    posX = PosX;
    posY = PosY;
    collectibleImage = colImg;
  }

  void update(float speed) {
    distMoved = speed/5;
    posY -= distMoved;
    rotation += distMoved;
  }

  void display() {
    pushMatrix();
    beginShape(QUADS);
    texture(collectibleImage);
    textureMode(NORMAL);
    //front
    translate(posX, posY, 25);
    scale(25);
    rotate(rotation*0.1);
    vertex(-1, 0, 1, 0, 0);
    vertex( 1, 0, 1, 1, 0);
    vertex( 1, 0, -1, 1, 1);
    vertex(-1, 0, -1, 0, 1);
    endShape();
    popMatrix();
  }

  boolean collideWith(float otherX, float otherY) {
    if (otherY > posY && otherY < posY + 32 && posX == otherX) {
      return true;
    }
    return false;
  }
}
