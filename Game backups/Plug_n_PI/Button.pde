class Button {
  int xPos, yPos;
  int Width, Height;
  color buttonColor = color(255);
  color HighlightColor = color(13,201,247);
  color lineColor = color(0);
  String buttonText;
  int toGameState;

  Button(int x, int y, int W, int H, String ButtonText, int ToGameState) {
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
      strokeWeight(4);
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
}
