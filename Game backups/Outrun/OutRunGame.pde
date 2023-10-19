class OutRunGame {
  PFont f; // initialize font object
  PGraphics blur;
  
  int cols, rows; // grid variables
  int scale = 20; // dimension of the square grid unit
  
  int w, h;
  float ws, hs;
  
  float speed = 0; // grid speed
  float speed_m = 0; // mountain speed
  
  float[][] terrain; // terrain grid
  int border = 20; // define the width of the valley
  
  // Player positions array
  int[] playerPos = {-80, 0, 80};
  int posIdx = 1;
  
  // Cars
  int dim = 5;
  Car[] cars;
  int counter = -1;
  boolean trig = false;
  int elapsed = millis();
  int score = 0;

  OutRunGame(int w, int h) {
    this.w = w;
    this.h = h;
    this.ws = w/800.0;
    this.hs = h/600.0;
    
    // Set up font
    f = createFont("Verdana", int(14*hs));
    textFont(f);
    textAlign(LEFT);
    
    // Set up world grid
    scale = int(20*ws);
    cols = int(1000.0*ws)/scale;
    rows = int(900.0*hs)/scale;
    
    // Set up mountains
    terrain = new float[cols][rows];
    
    // Set up cars
    cars = new Car[dim];
  }
  
  void update(float dt) {
    // Set speeds
    speed -= (4*hs)*dt;
    //println(dt);
    if (speed<=-scale){
      speed=0;
    }
    speed_m -= (0.1*hs)*dt;
    
    // Set perlin noise for mountains
    //float y_offset = speed_m;
    //for (int y = 0; y < rows; y++) {
    //  float x_offset = 0;
    //  for (int x = 0; x < cols; x++) {
    //    terrain[x][y] = map(noise(x_offset, y_offset), 0, 1, -50, 150);
    //    x_offset += 0.2;
    //  }
    //  terrain[border-1][y] = 0;
    //  terrain[cols-border+1][y] = 0;
    //  y_offset += 0.2;
    //}
    
    // Update cars and collision
    trigger();
    if (trig == true) {
      if(++counter == dim){
        counter = 0;
      }   
      cars[counter] = new Car(w, h);
      trig = false;
    }
    for (int i=0; i<dim; i++) {
      if (cars[i]!=null){
        cars[i].update(dt);
        float[] collision = cars[i].getPosition();
        // Check collision
        if (collision[0] == playerPos[posIdx] && collision[1]<380 && collision[1]>300) {
          score = 0;
          cars[i].hasPassed = true;
        } 
        if (collision[0] != playerPos[posIdx] && collision[1]>=380 && !cars[i].hasPassed){
          score++;
          cars[i].hasPassed = true;
        }
      }
    }
  }
  
  void displayBackground() {
    // Set background
    background(0);
    setGradient((int)(-800*ws), (int)(-900*hs), (int)(-1000/600*h), 3500*ws, 1200*hs, color(0,0,130), color(0,0,0));
    
   //Sun
    noStroke();
    fill(255,100,25);
    pushMatrix();
    translate(w/2, h/2-100*w/800.0, -400*hs);
    circle(0, 0, 400*ws);
    popMatrix();
  }
  
  void display() {    
    // Draw fps on screen
    fill(255);
    text("FPS: " + frameRate, 5*ws, 20*hs);
    // Draw score
    text("Score: " + score, w-100*ws, 20*hs);
    
    // Translate and rotate world
    translate(w/2, h/2);
    rotateX(PI/2.2); 
    
    // Draw central grid
    stroke(217,25,255);
    pushMatrix();
    translate(-int(w/800.0*1000.0)/2, -int(h/600.0*900.0)/2-speed);
    noFill();
    for (int y = 0; y < rows-1; y++) {
      for (int x = border-1; x < cols-border+1; x++) {
        rect(x*scale+1, y*scale+1, scale, scale);
      }
    }
    popMatrix();
    
    //// Draw lateral mountains
    //pushMatrix();
    //translate(-w/2, -h/2-0.2);
    
    //fill(0,0,200);
    //for (int y = 0; y < rows-1; y++) {
    //  beginShape(TRIANGLE_STRIP);
    //  for (int x = 0; x < border; x++) {
    //    vertex(x*scale, y*scale, terrain[x][y]);
    //    vertex(x*scale, (y+1)*scale, terrain[x][y+1]);
    //  }
    //  endShape();
    //}
    //for (int y = 0; y < rows-1; y++) {
    //  beginShape(TRIANGLE_STRIP);
    //  for (int x = cols-border+1; x < cols; x++) {
    //    vertex(x*scale, y*scale, terrain[x][y]);
    //    vertex(x*scale, (y+1)*scale, terrain[x][y+1]);
    //  }
    //  endShape();
    //}
    //popMatrix();
    
    for (int i=0; i<dim; i++) {
      if (cars[i]!=null){
        cars[i].display();
      }
    }
    
    // Draw player
    fill(255);
    pushMatrix();
    translate((playerPos[posIdx]*ws), (380*hs), 5);
    box((40*ws), (50*hs), (10*hs));
    popMatrix();
  }
  
  // Custom function for generating a gradient
  void setGradient(int x, int y, int z, float w, float h, color c1, color c2) {
    noFill();
    for (int i = y; i <= y+h; i++) {
      float inter = map(i, y, y+h, 0, 1);
      color c = lerpColor(c1, c2, inter);
      stroke(c);
      line(x, i, z, x+w, i, z);
    }
  }
  
  void trigger() {
    if (millis() > elapsed + int(random(1000, 2000))) {
      trig = true;
      elapsed = millis();
    }
  }
  
  void moveDelta(int delta) {
    moveLane(delta+posIdx);
  }
  
  void moveLane(int lane) {
    if (lane >= 0 && lane < 3) {
      posIdx = lane;
    } else if (lane < 0) {
      posIdx = 0;
    } else if (lane > 2) {
      posIdx = 2;
    }
  }
}
