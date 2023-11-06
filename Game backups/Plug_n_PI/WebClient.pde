import com.cage.zxing4p3.*;
//import processing.net.*;
import http.requests.*;

enum OnlineState {
  Connecting,
  QRCode,
  Ready,
  Uploading,
  Offline
}

class WebClient {
  ZXING4P zxing4p;
  int QRCodeSize = 128;
  int fontSize = 20;
  
  float completeCycleTime = 100;
  float CycleTime = 0;

  String host = "http://145.126.2.121:8080";
  
  String QRCodeContent = "https://www.youtube.com/watch?v=KMU0tzLwhbE";
  PImage QRCode;
  String sessionID = "";
  String username = "None"; 
  String highscoreString = "";
  
  PApplet papplet;
  
  OnlineState onlineState;

  WebClient(PApplet papplet) {
    this.papplet = papplet;
    zxing4p = new ZXING4P();
    onlineState = OnlineState.Connecting;
    getRequest("/plugnpi/api/pi");
    updateQRCode();
  }

  void update(float dt) {
    //if(!waitingForData && !client.active() && onlineState == OnlineState.QRCode) //<>//
    if(onlineState == OnlineState.QRCode)
    {
      CycleTime += dt;
      if (CycleTime > completeCycleTime) {
        CycleTime = 0;
        getRequest("/plugnpi/api/pi/link?session=" + sessionID + "&action=request_user");
      }
    }
  }
  
  void uploadScore(int score) {
    //String[][] request = {{"pid", sessionID}, {"distance", Integer.toString(score)}};
    //postRequest("/plugnpi/api/leaderboard", request);
    String request = 
        "{\"pid\":\"" 
      + sessionID 
      + "\", \"distance\":\"" 
      + Integer.toString(score) 
      + "\"}";
    println("Request: " + request);
    postJsonRequest("/plugnpi/api/leaderboard", request);
  }

  //void receiveData() {
  //  while (client.available() > 0)
  //  {
  //    String output = client.readStringUntil('\n');
  //    if (output == null)
  //    {
  //      println("Output was null");
  //      client.clear();
  //      break;
  //    }
  //    output = output.trim();
  //    parseString(output);
  //  }
  //  waitingForData = false;
  //}
  
  void receiveData(String data) {
    String split[] = data.split("\\R");
    for (int i = 0; i < split.length; i++)
    {
      String line = split[i];
      line = line.trim();
      parseString(line);
    }
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
      if(onlineState == OnlineState.Connecting) {
        onlineState = OnlineState.QRCode;
      }
      updateSessionID(content);
      println("Received session code.\n");
      break;
    case "username":
      username = content;
      connectedUserName = content;
      onlineState = OnlineState.Ready;
      println("Received username: " + content);
      break;    
    default:
      println("Data Type not recognised.\n");
    }
  }

  void updateSessionID(String newSessionID) {
    sessionID = newSessionID;
    String connectURL = host + "/plugnpi/api/pi/link?session=" + sessionID + "&action=connect";
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
  
  void updateHighscores() {
    String HighscoresRaw = getJsonRequest("/plugnpi/api/leaderboard?date=weekly");
    JSONArray highscoresJson = parseJSONArray(HighscoresRaw);
    if(highscoresJson == null) return;
    
    highscoreString = "Weekly highscores:\n";
    for(int i = 0; i < highscoresJson.size(); i++) {
      JSONObject highscoreObject = highscoresJson.getJSONObject(i);
      highscoreString += i+1 + ". " + highscoreObject.getString("username") + ": " + parseInt(highscoreObject.getInt("distance")) + "\n";
    }
  }

  //void webRequest(String request) {
  //  client = new Client(papplet, host, port);
  //  client.write(request + "\n");
  //  client.write("Host: " + host + "\n\n");
  //  waitingForData = true;
  //  println("Send: \"" + request + "\".\n");
  //}
  
  void getRequest(String request) {
    GetRequest get = new GetRequest(host + request);
    get.send();
    println("Reponse Content: " + get.getContent());
    //println("Reponse Content-Length Header: " + get.getHeader("Content-Length"));
    if(get.getContent() != null) {
      receiveData(get.getContent());
    }
  }
  
  String getJsonRequest(String request) {
    GetRequest get = new GetRequest(host + request);
    get.send();
    println("Reponse Content: " + get.getContent());
    //println("Reponse Content-Length Header: " + get.getHeader("Content-Length"));
    if(get.getContent() != null) {
      return get.getContent();
    }
    return("");
  }
  
  void postRequest(String request, String[][] data) {
    PostRequest post = new PostRequest(host + request);
    for(int i = 0; i < data.length; i++)
    {
      post.addData(data[i][0], data[i][1]);
    }
    post.send();
    println("Reponse Content: " + post.getContent());
    //println("Reponse Content-Length Header: " + post.getHeader("Content-Length"));
    if(post.getContent() != null) {
      receiveData(post.getContent());
    }
  }
  
    void postJsonRequest(String request, String json) {
    PostRequest post = new PostRequest(host + request);
    post.addHeader("Content-Type", "application/json");
    post.addData(json);
    post.send();
    println("Reponse Content: " + post.getContent());
    println("Reponse Content-Length Header: " + post.getHeader("Content-Length"));
    if(post.getContent() != null) {
      receiveData(post.getContent());
    }
  }

  //void display() {
  //  pushMatrix();
  //  translate(width/2, 520);
  //  scale(2);
  //  imageMode(CENTER);
  //  image(QRCode, 0, 0);
  //  popMatrix();
  //  textSize(fontSize);
  //  textAlign(CENTER);
  //  text("Scan the QR code to connect, or enter the address manually:",width/2, 410);
  //  text(QRCodeContent, width/2, 410 + fontSize);
  //}
  
  PImage getQRCode() {
    return QRCode;
  }
  
  String getQRCodeContent() {
    return QRCodeContent;
  }
  
  String getHighscores() {
    return highscoreString;
  }
}
