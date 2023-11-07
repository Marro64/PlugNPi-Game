class GroundGrid {
  float sizeX, sizeY;
  int rows, columns;
  PImage groundImage;
  float scale;
  float gScale;
  float gameW;
  float gameH;
  float distMoved = 0;
  float speed;
  int border = 5;

  GroundGrid(int Rows, int Columns, float GameW, float GameH, float Scale) {


    //set scale and gamesize
    gScale = Scale;
    scale = 64;
    border = 0;
    gameW = GameW;
    gameH = GameH;

    //setup rows and columns
    rows = Rows;
    columns = Columns;

    //loadimage
    groundImage = loadImage("groundImage.png");
  }

  void display() {
    noStroke();
    pushMatrix();
    noFill();
    beginShape(QUADS);
    scale(gScale);
    scale(scale);
    translate(-columns/2, -rows/2 - distMoved);
    texture(groundImage);
    for (int y = 0; y < rows-1; y++) {
      for (int x = border-1; x < columns-border-1; x++) {
        vertex(x+0.5, y-0.5, 0, 0, 0);
        vertex( x+1.5, y-0.5, 0, 1, 0);
        vertex( x+1.5, y+0.5, 0, 1, 1);
        vertex(x+0.5, y+0.5, 0, 0, 1);
      }
    }
    endShape();
    popMatrix();
  }

  void update(float speed) {
    distMoved = speed/5;
  }
  
  void rescale(float Scale){
    gScale = Scale;
  }
}
