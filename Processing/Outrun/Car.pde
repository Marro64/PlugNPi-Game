class Car {
  
  int speed = 0;
  int[] pos = {-80, 0, 80};
  int posIdx;
  int posY;
  boolean hasPassed = false;
  
  // Contructor
  Car() {
    posIdx = int(random(3));
  }
  
  // Custom method for updating the variables
  void update(float dt) {
    speed -= 4*dt;
    posY = -300-speed;
  }
  
  // Custom method for drawing the object
  void display() { 
    stroke(255);
    fill(0,70,25);
    pushMatrix();
    translate(pos[posIdx], posY, 5);
    box(40, 50, 10);
    popMatrix();
  }
  
  int[] getPosition() {
    int[] vec = {pos[posIdx], posY};
    return vec;
  }
  
  boolean hasGivenPoint() {
    return hasGivenPoint;
  }
}
