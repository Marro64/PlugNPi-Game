import com.cage.zxing4p3.*;
import processing.net.*;

class WebClient {
  ZXING4P zxing4p;
  String QRCodeContent = "https://www.youtube.com/watch?v=KMU0tzLwhbE";
  PImage QRCode;
  int QRCodeSize = 128;
  int fontSize = 20;
  float completeCycleTime = 5;
  float CycleTime = 0;

  Client client;
  String host = "145.126.2.121";
  int port = 8080;
  boolean waitingForData = false;
  
  String sessionID = "";
  String username = "None"; 
  
  PApplet papplet;

  WebClient(PApplet papplet) {
    this.papplet = papplet;
    zxing4p = new ZXING4P();
    webRequest("GET /plugnpi/api/pi HTTP/1.0");
    updateQRCode();
  }

  void update(float dt) {
    if(!waitingForData && !client.active()) //<>//
    {
      CycleTime += dt;
      if (CycleTime > completeCycleTime) {
        CycleTime = 0;
        webRequest("GET /plugnpi/api/pi/getUsername?session=" + sessionID + " HTTP/1.0");
      }
    }
    
    //if (waitingForData && !client.active())
    //{
      receiveData();
    //}
  }

  void receiveData() {
    while (client.available() > 0)
    {
      String output = client.readStringUntil('\n');
      if (output == null)
      {
        println("Output was null");
        //client.clear();
        break;
      }
      output = output.trim();
      parseString(output);
    }
    waitingForData = false;
  }

  void parseString(String data) {
    println("Received: \"" + data + "\"");
    int delimiterIndex = data.indexOf(':');
    if (delimiterIndex == -1) {
      println("No delimiter.\n");
      return;
    }
    String dataType = data.substring(0, delimiterIndex).trim();
    String content = data.substring(delimiterIndex+1).trim();
    println("Data Type: " + dataType);
    println("Content: " + content);
    switch(dataType) {
    case "session":
      updateSessionID(content);
      println("Received session code.\n");
      break;
    case "username":
      username = content;
      connectedUserName = content;
      isConnected = true;
      gameState = 2;
      println("Received username: " + content);
      break;    
    default:
      println("Data Type not recognised.\n");
    }
  }

  void updateSessionID(String newSessionID) {
    sessionID = newSessionID;
    String connectURL = "http://" + host + ":" + port + "/plugnpi/api/pi/link?session=" + sessionID + "&action=connect";
    updateQRCode(connectURL);
  }

  void updateQRCode() {
    QRCode = zxing4p.generateQRCode(QRCodeContent, 128, 128);
  }

  void updateQRCode(String content) {
    QRCodeContent = content;
    QRCode = zxing4p.generateQRCode(content, 128, 128);
    println("Set QR code to \"" + content + "\".\n");
  }

  void webRequest(String request) {
    client = new Client(papplet, host, port);
    client.write(request + "\n");
    client.write("Host: " + host + "\n\n");
    waitingForData = true;
    println("Send: \"" + request + "\".\n");
  }

  void display() {
    pushMatrix();
    translate(width/2, 520);
    scale(2);
    imageMode(CENTER);
    image(QRCode, 0, 0);
    popMatrix();
    textSize(fontSize);
    textAlign(CENTER);
    text("Scan the QR code to connect, or enter the address manually:",width/2, 410);
    text(QRCodeContent, width/2, 410 + fontSize);
  }
}
