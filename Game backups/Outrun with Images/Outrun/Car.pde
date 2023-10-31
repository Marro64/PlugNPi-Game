class Car {

  float speed = 0;
  int[] pos = {-80, 0, 80};
  int posIdx;
  float posY;
  boolean hasPassed = false;
  float w, h;
  float ws, hs;
  PImage carImage;

  // Contructor
  Car(int w, int h, PImage CarImage) {
    posIdx = int(random(3));
    this.ws = w/800.0;
    this.hs = h/600.0;
    carImage = CarImage;

  }

  // Custom method for updating the variables
  void update(float dt) {
    speed -= 4*dt;
    posY = -300-speed;
  }

  // Custom method for drawing the object
  void display() {
    //stroke(255);
    fill(0, 70, 25);
    pushMatrix();
    noStroke();
    translate((pos[posIdx]*ws), (posY*hs), 32);
    beginShape(QUADS);
    texture(carImage);
    scale(32);
    vertex(-1, 1, 1, 0, 0);
    vertex( 1, 1, 1, 1, 0);
    vertex( 1, 1, -1, 1, 1);
    vertex(-1, 1, -1, 0, 1);
    endShape();
    //beginShape(QUADS);
    //texture(carImage);
    //endShape();
    popMatrix();
  }

  float[] getPosition() {
    float[] vec = {pos[posIdx], posY};
    return vec;
  }
}
