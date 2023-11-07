class RunnerGame {
  int cols, rows; // grid variables
  float scale = 1; //scale of the game
  float gameW;
  float gameH;
  float startSpeed;
  float acceleration;
  float speed = 0; // game speed
  int maxDistMoved = 32;
  float distMoved = 0;
  float walktimer = 0;

  int border = 250; // define the width of the outer edges

  // Player position
  int posIdx = 1;
  float playerRotation = 0;
  boolean doAflip = false;
  float flipSpeed = 0.6;
  
  float menuY = 0;

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

  //Collectibles
  PImage collectibleImg;
  Collectible[] collectibles;
  float collectibleSpawnTime = 5000;
  int dimCol = 30;
  int colCounter = -1;
  boolean spawnCollectible = false;
  int elapsedCollectible = millis();
  int colScore;
  int lives = 0;

  int score = 0;
  int gameHighScore = 0;
  boolean newHighScore = false;
  PImage trainFront;
  PImage trainSide;
  PImage trainTop;

  PImage playerImage;
  int fontSize = 30;

  //grid
  GroundGrid groundGrid;

  RunnerGame(int w, int h) {

    //setup game variables
    startSpeed = 3;
    speed = startSpeed;
    acceleration = 0.1;

    gameW = w;
    gameH = h;

    laneXpos[0] = -64;
    laneXpos[2] = 64;

    textureMode(NORMAL);

    // Set up world grid
    groundGrid = new GroundGrid(25, 3, w, h, scale);

    // Set up trains
    trainFront = loadImage("Train_Front.png");
    trainSide = loadImage("Train_Side.png");
    trainTop =loadImage("Train_Top.png");
    trains = new Train[dim];

    playerImage = loadImage("runnerKid.png");

    //
    collectibleImg = loadImage("Coin.png");
    collectibles = new Collectible[dimCol];
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
          lives--;
          playFailsfx();
          speed = (speed-startSpeed)/2 + startSpeed;
          if (lives < 0) {
            endGame();
          } else {
            resetGameObjects();
          }
        } else if (train.posY > gameH*0.6 && !train.hasPassed) {
          train.hasPassed = true;
          //playDopaminesfx();
          speed += acceleration;
          score++;
        }
      }
    }

    //update collectibles
    colTrigger();

    if (spawnCollectible == true) {
      if (++colCounter == dimCol) {
        colCounter = 0;
      }
      collectibles[colCounter] = new Collectible(collectibleImg, laneXpos[int(random(3))], trainStartY);
      spawnCollectible = false;
    }

    for (Collectible collectible : collectibles) {
      if (collectible != null) {
        collectible.update(speed, dt);
        if (collectible.collideWith(laneXpos[posIdx], gameH*0.6)) {
          playDopaminesfx();
          colScore ++;
          if (colScore > 99) {
            colScore = 0;
            lives ++;
          }
          collectible.posY = 10000;
        } else if (collectible.posY > gameH*0.6 && !collectible.hasPassed) {
          collectible.hasPassed = true;
        }
      }
    }
    
    if(score > gameHighScore) {
      newHighScore = true;
      gameHighScore = score;
    }
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

  void display() {
    pushMatrix();

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

    for (Collectible collectible : collectibles) {
      if (collectible != null) {
        collectible.display();
      }
    }

    // Draw player
    displayPlayer();
    popMatrix();
  }

  void trigger() {
    if (millis() > elapsed + trainSpawnTime*(1/speed)) {
      trig = true;
      elapsed = millis();
    }
  }

  void colTrigger() {
    if (millis() > elapsedCollectible + collectibleSpawnTime*(1/speed)) {
      spawnCollectible = true;
      elapsedCollectible = millis();
    }
  }

  void moveDelta(int lane) {
    int delta = 0;
    if (lane > posIdx) {
      delta = 1;
    } else if (lane < posIdx) {
      delta  =-1;
    }
    moveLane(delta);
  }

  void moveLane(int delta) {
    int oldlane = posIdx;
    int lane  = posIdx + delta;
    if (lane >= 0 && lane < 3) {
      posIdx = lane;
    } else if (lane < 0) {
      posIdx = 0;
    } else if (lane > 2) {
      posIdx = 2;
    }
    if (oldlane != posIdx) {
      flipSpeed = delta * abs(flipSpeed);
      doAflip = true;
    }
  }

  void displayPlayer() {

    beginShape(QUADS);
    texture(playerImage);
    textureMode(NORMAL);
    //front
    walktimer -= 3;
    calculateFlip();
    translate(laneXpos[posIdx]*scale, gameH*0.6, 30 + 2*sin(walktimer*0.1));
    int rotationDirection = 1;
    if (doAflip) {
      if (playerRotation >0){
        rotationDirection= -1;
      }
      translate(64*rotationDirection+(64/(2*PI))*playerRotation, 0,3*(pow(PI, 2)-pow(abs(playerRotation)-PI, 2)));
    }
    scale(23, 32, 32);
    scale(0.8);
    rotateY(playerRotation);
    vertex(-1, 0, 1, 0, 0);
    vertex( 1, 0, 1, 1, 0);
    vertex( 1, 0, -1, 1, 1);
    vertex(-1, 0, -1, 0, 1);
    endShape();
  }

  void reset() {
    score = 0;//reset score
    colScore = 0;
    lives = 0;
    newHighScore = false;
    resetGameObjects();
  }

  void calculateFlip() {
    if (doAflip) {
      playerRotation += flipSpeed;
      if (playerRotation > 2*PI || playerRotation < -2*PI) {
        playerRotation = 0;
        doAflip = false;
      }
    }
  }

  void resetGameObjects() {
    speed = startSpeed;
    for (Train train : trains) {//reset trains
      if (train != null) {
        train.reset();
      }
    }
    for (Collectible collectible : collectibles) {//reset trains
      if (collectible != null) {
        collectible.reset();
      }
    }
  }
  
  boolean getNewHighScore() {
    return newHighScore;
  }
  
  void rescale(int w, int h){
    gameW = w;
    scale = scale * float(h)/gameH;
    groundGrid.rescale(scale);
    gameH = h;
        for (Train train : trains) {//reset trains
      if (train != null) {
        train.rescale(scale);
      }
    }
  }
}
