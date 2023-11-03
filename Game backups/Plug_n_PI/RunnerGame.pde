class RunnerGame {
  PGraphics blur;

  int cols, rows; // grid variables
  int scale = 1; //scale of the game
  float gameW;
  float gameH;
  float startSpeed;
  float acceleration;
  float speed = 0; // game speed
  int maxDistMoved = 32;
  float distMoved = 0;

  int border = 250; // define the width of the outer edges

  // Player position
  int posIdx = 1;

  //possible lane locations
  float[] laneXpos = {0, 0, 0};

  // trains
  float trainStartY = -300;
  float trainSpawnTime = 5000;

  int dim = 5;
  Train[] trains;
  int counter = -1;
  boolean trig = false;
  int elapsed = millis();
  int score = 0;
  int endscore = 0;
  int gameHighScore = 0;
  PImage trainFront;
  PImage trainSide;
  PImage trainTop;

  //grid
  GroundGrid groundGrid;

  RunnerGame(int w, int h) {

    //setup game variables
    startSpeed = 3;
    speed = startSpeed;
    acceleration = 0.05;

    gameW = w;
    gameH = h;

    laneXpos[0] = -56;
    laneXpos[2] = 56;

    textureMode(NORMAL);

    // Set up world grid
    groundGrid = new GroundGrid(25, 3, w, h, scale);

    // Set up trains
    trainFront = loadImage("Train_Front.png");
    trainSide = loadImage("Train_Side.png");
    trainTop =loadImage("Train_Top.png");
    trains = new Train[dim];
  }

  void update(float dt) {
    // Set speeds
    distMoved -= speed*dt;
    //println(dt);
    if (distMoved <=-maxDistMoved) {
      distMoved=0;
    }

    trigger();

    if (trig == true) {
      if (++counter == dim) {
        counter = 0;
      }
      trains[counter] = new Train(trainFront, trainSide, trainTop, laneXpos[int(random(3))], trainStartY);
      trig = false;
    }

    for (Train train : trains) {
      if (train != null) {
        train.update(dt, speed);
        if (train.collideWith(laneXpos[posIdx], gameH*0.6)) {
          endscore = score;
          reset();
          playFailsfx();
          if (isConnected) {
            gameState =99;
          }
        } else if (train.posY > gameH*0.6 && !train.hasPassed) {
          train.hasPassed = true;
          playDopaminesfx();
          speed += acceleration;
          score++;
        }
      }
    }

    //groundgrid
    groundGrid.update(distMoved);
  }

  void displayBackground() {
    // Set background
    background(color(29, 17, 67));

    //Sun
    noStroke();
    fill(254, 195, 8);
    pushMatrix();
    translate(gameW/2, gameH/2-80*gameW/800.0, -gameH/2 + 100);
    circle(0, 0, gameW/4);
    popMatrix();
  }

  void display(Capture video) {
    pushMatrix();
    // Draw fps on screen
    fill(255);
    text("FPS: " + frameRate, 20, 60);

    // Draw score
    textAlign(LEFT);
    text("Score: " + score, 20, 20);
    if (gameHighScore > 0)text("Highscore: " + gameHighScore, 20, 40);

    // Translate and rotate world
    translate(gameW/2, gameH/2);
    rotateX(PI/2.2);

    // Draw central grid
    groundGrid.display();

    //Display trains
    for (Train train : trains) {
      if (train != null) {
        train.display();
      }
    }

    // Draw player
    displayPlayer(video);
    popMatrix();
  }

  void trigger() {
    if (millis() > elapsed + trainSpawnTime*(1/speed)) {
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

  void displayPlayer(Capture video) {

    beginShape(QUADS);
    texture(video);
    textureMode(NORMAL);
    //front
    translate(laneXpos[posIdx], gameH*0.6, 5);
    scale(25);
    vertex(-1, 1, 1, 0, 0);
    vertex( 1, 1, 1, 1, 0);
    vertex( 1, 1, -1, 1, 1);
    vertex(-1, 1, -1, 0, 1);
    endShape();
  }

  void reset() {
    backgroundMusic.rewind();
    backgroundMusic.play();
    speed = startSpeed;
    endscore = score;
    score = 0;//reset score
    if (endscore > gameHighScore) gameHighScore = endscore;
    for (Train train : trains) {//reset trains
      if (train != null) {
        train.reset();
      }
    }
  }
}
