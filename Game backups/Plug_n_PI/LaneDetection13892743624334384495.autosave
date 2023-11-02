import java.awt.*;
import gab.opencv.*;

import processing.video.*;

Capture video;

OpenCV opencv;

int capWidth;
int capHeight;
int Cscale;
String lane;
boolean showCamera;
boolean showRectangles;
boolean showIndicator = true;

Rectangle[] faces;

class lanedetection {
  lanedetection(PApplet papplet, int cWidth, int cHeight) {
    showCamera = false;
    showRectangles = false;
    lane = "middle";
    capWidth = cWidth;
    capHeight = cHeight;
    Cscale = 4;

    video = new Capture(papplet, capWidth/Cscale, capHeight/Cscale);

    opencv = new OpenCV(papplet, capWidth/Cscale, capHeight/Cscale);
    opencv.loadCascade(OpenCV.CASCADE_FRONTALFACE);
    video.start();
  }

  void update() {
    opencv.loadImage(video);
    faces = opencv.detect();
    if (faces.length >0) {
      detect_lane(faces[0].x + faces[0].width/2);
    }
  }

  void display() {
    if (showIndicator) {
      displayIndicator();
    }
    if (showRectangles) {
      pushMatrix();
      scale(Cscale);
      drawRectangles(faces);
      popMatrix();
    }

    if (showCamera) {
      pushMatrix();
      scale(Cscale);
      drawCamera();
      popMatrix();
    }
  }

  void drawRectangles(Rectangle[] faces) {
    noFill();
    stroke(0, 255, 0);
    strokeWeight(3);
    for (int i = 0; i < faces.length; i++) {
      rect((width-faces[i].x)/Cscale, faces[i].y, (width-faces[i].width)/Cscale, faces[i].height);
    }
  }

  void displayIndicator() {
    pushMatrix();
    hint(DISABLE_DEPTH_TEST);
    fill(200);
    rect(width/2, height-15, capWidth, 10);
    fill(255, 0, 0);
    if (faces.length >0) {
      rect(width/2 + float(capWidth)/2 - (faces[0].x + faces[0].width/2)*Cscale, height -15, 10, 10);
    }
    hint(ENABLE_DEPTH_TEST);
    popMatrix();
  }

  void drawCamera() {
    image(video, 0, 0 );
  }

  void detect_lane(float FaceCenter) {
    String newlane;
    if ((capWidth/Cscale)/3>FaceCenter) {
      newlane = "right";
      moveLane(2);
    } else if (FaceCenter>(((capWidth/Cscale)/3)*2)) {
      newlane = "left";
      moveLane(0);
    } else {
      newlane = "middle";
      moveLane(1);
    }
    if (newlane == lane) {
      return;
    } else {
      lane = newlane;
      println("switched to lane: " + lane);
    }
  }

  String getlane() {
    return lane;
  }
  
  Capture passvideo(){
    return video;
  }
}
