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

  //Collectibles
  PImage collectibleImg;
  Collectible[] collectibles;
  float collectibleSpawnTime = 2000;
  int dimCol = 100;
  int colCounter = -1;
  boolean spawnCollectible = false;
  int elapsedCollectible = millis();

  int score = 0;
  int endScore = 0;
  int gameHighScore = 0;
  PImage trainFront;
  PImage trainSide;
  PImage trainTop;
  
  int fontSize = 30;

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
          endScore = score;
          reset();
          playFailsfx();
          uploadScore(endScore);
          gameState = GameState.GameOver;
          //if (isOnline()) {
          //  // Upload Score
          //}
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
        collectible.update(distMoved);
        if (collectible.collideWith(laneXpos[posIdx], gameH*0.6)) {
          playDopaminesfx();
          score += 5;
        } else if (collectible.posY > gameH*0.6 && !collectible.hasPassed) {
          collectible.hasPassed = true;
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
    textSize(fontSize);
    textAlign(LEFT);
    fill(255);
    
    // Draw score
    text("Score: " + score, 20, fontSize);
    if (gameHighScore > 0)text("Highscore: " + gameHighScore, 20, fontSize*2);
    
    // Draw fps on screen
    text("FPS: " + frameRate, 20, fontSize*3);

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
    displayPlayer(video);
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
    endScore = score;
    score = 0;//reset score
    if (endScore > gameHighScore) gameHighScore = endScore;
    for (Train train : trains) {//reset trains
      if (train != null) {
        train.reset();
      }
    }
  }
}
