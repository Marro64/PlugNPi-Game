class Car {
  
  float speed = 0;
  int[] pos = {-80, 0, 80};
  int posIdx;
  float posY;
  boolean hasPassed = false;
  float w, h;
  float ws, hs;
  
  // Contructor
  Car(int w, int h) {
    posIdx = int(random(3));
    this.ws = w/800.0;
    this.hs = h/600.0;
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
    translate((pos[posIdx]*ws), (posY*hs), 5);
    box((40*ws), (50*hs), (10*hs));
    popMatrix();
  }
  
  float[] getPosition() {
    float[] vec = {pos[posIdx], posY};
    return vec;
  }
}
