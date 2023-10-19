import processing.video.*;
import java.awt.*;
import gab.opencv.*;

Capture video;
OpenCV opencv;

int capWidth;
int capHeight;
int Dscale;
String lane;
boolean showCamera;
boolean showRectangles;

Rectangle[] faces;

class lanedetection {
  lanedetection(PApplet papplet, int cWidth, int cHeight) {
    showCamera = true;
    showRectangles = true;
    lane = "middle";
    capWidth = cWidth;
    capHeight = cHeight;
    Dscale = 2*(int)(width/cWidth);
    video = new Capture(papplet, capWidth/2, capHeight/2);
    opencv = new OpenCV(papplet, capWidth/2, capHeight/2);
    opencv.loadCascade(OpenCV.CASCADE_FRONTALFACE);
    video.start();
  }

  void update() {
    faces = opencv.detect();
    opencv.loadImage(video);
    if (faces.length >0) {
      detect_lane(faces[0].x);
    }
  }
  
  void display() {
    pushMatrix();
    scale(Dscale);
    if (showRectangles) {
      this.drawRectangles(faces);
    }

    if (showCamera) {
      this.drawCamera();
    }
    popMatrix();
  }

  void drawRectangles(Rectangle[] faces) {
    noFill();
    stroke(0, 255, 0);
    strokeWeight(3);
    for (int i = 0; i < faces.length; i++) {
      rect(faces[i].x, faces[i].y, faces[i].width, faces[i].height);
    }
  }

  void drawCamera() {
    image(video, 0, 0 );
  }

  void detect_lane(float FaceCenter) {
    String newlane;
    if (capWidth/(3*Dscale)>FaceCenter) {
      newlane = "right";
      moveLane(2);
    } else if (FaceCenter>(capWidth/(3*Dscale))*2) {
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
  
  String getlane(){
    return lane;
  }
}
