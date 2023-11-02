import com.cage.zxing4p3.*;
import processing.net.*;  

class WebClient {
  ZXING4P zxing4p;
  String QRCodeContent = "https://www.youtube.com/watch?v=KMU0tzLwhbE";
  PImage QRCode;
  int QRCodeSize = 128;
  int fontSize = 20;
  
  Client client;
  String host = "145.126.2.121";
  int port = 8080; 
  boolean waitingForData = false;
  
  WebClient(PApplet papplet) {
    zxing4p = new ZXING4P();
    client = new Client(papplet, host, port);
    webRequest("GET /plugnpi/api/pi HTTP/1.0");
    updateQRCode();
  }
  
  void update() {
    if(waitingForData && !client.active())
    {
      receiveData();
    }
  }
  
  void receiveData() {
    while(client.available() > 0)
    {
      String output = client.readStringUntil('\n');
      if(output == null) 
      {
        println("Output was null");
        client.clear();
        break;
      }
      output = output.trim();
      parseString(output);
    }
  }
  
  void parseString(String data) {
    println("Received: \"" + data + "\"");
    int delimiterIndex = data.indexOf(':');
    if(delimiterIndex == -1) {
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
      default:
        println("Data Type not recognised.\n");
    }
  }
  
  void updateSessionID(String sessionID) {
    String connectURL = "http://" + host + ":" + port + "/plugnpi/api/pi/link?session=" + sessionID + "&connect=true";
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
    client.write(request + "\n");
    client.write("Host: " + host + "\n\n");
    waitingForData = true;
    println("Send: \"" + request + "\".\n"); 
  }
  
  void display() {
    textSize(fontSize);
    textAlign(LEFT, TOP);
    text(QRCodeContent, 0, 10);
    imageMode(CORNER);
    image(QRCode, 0, 10+2*fontSize);
  }
}
