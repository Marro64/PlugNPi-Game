class Button {
  int xPos, yPos;
  int Width, Height;
  int originalX, originalY, originalW, originalH;
  int originalScreenWidth, originalScreenHeight;
  color buttonColor = color(255);
  color HighlightColor = color(13,201,247);
  color lineColor = color(0);
  String buttonText;
  GameState toGameState;

  Button(int x, int y, int W, int H, String ButtonText, GameState ToGameState) {
    originalScreenWidth = width;
    originalScreenHeight = height;
    originalX = x;
    originalY = y;
    originalW = W;
    originalH = H;
    buttonText = ButtonText;
    xPos = x;
    yPos = y;
    Width = W;
    Height = H;
    toGameState = ToGameState;
  }

  void display(int mX, int mY) {
    stroke(lineColor);
    rectMode(CENTER);
    if (isInbox(mX, mY)) {
      strokeWeight(5);
      fill(HighlightColor);
    } else {
      strokeWeight(2);
      fill(buttonColor);
    }
    rect(xPos, yPos, Width, Height);
    fill(0);
    textSize(20);
    textAlign(CENTER);
    text(buttonText, xPos,yPos);
  }

  boolean isInbox(int mX, int mY) {
    if (mX >= xPos - Width/2 && mX <= xPos+Width/2 && mY >= yPos -Height/2 && mY <= yPos+Height/2) {
      return true;
    } else {
      return false;
    }
  }
  
  void rescale(int w, int h) {
    xPos = (int)(originalX/(float)originalScreenWidth * w);
    yPos = (int)(originalY/(float)originalScreenHeight * h);
    Width = (int)(originalW/(float)originalScreenWidth * w);
    Height = (int)(originalH/(float)originalScreenHeight * h);
  }
}
