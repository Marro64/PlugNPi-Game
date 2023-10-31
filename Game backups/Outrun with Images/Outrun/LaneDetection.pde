import java.awt.*;
import gab.opencv.*;

//import gohai.glvideo.*;
import processing.video.*;

//GLCapture video;
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
    showCamera = false;
    showRectangles = false;
    lane = "middle";
    capWidth = cWidth;
    capHeight = cHeight;
    Dscale = 3;
    
    video = new Capture(papplet, capWidth/2, capHeight/2);
    
    //String[] devices = GLCapture.list();
    //println("Devices:");
    //printArray(devices);
    //if (0 < devices.length) {
    //  String[] configs = GLCapture.configs(devices[0]);
    //  println("Configs:");
    //  printArray(configs);
    //}
    //video = new GLCapture(papplet);
    
    opencv = new OpenCV(papplet, capWidth/2, capHeight/2);
    opencv.loadCascade(OpenCV.CASCADE_FRONTALFACE);
    video.start();
  }

  void update() {
    //if (video.available()) {
    //  video.read();
    //}
    opencv.loadImage(video);
    faces = opencv.detect();
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
      //rect((width-faces[i].x)/Dscale, faces[i].y, (width-faces[i].width)/Dscale, faces[i].height);
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
