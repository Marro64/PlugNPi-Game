import com.cage.zxing4p3.*;
//import processing.net.*;
import http.requests.*;

enum OnlineState {
  Connecting,
  QRCode,
  Ready
}

class WebClient {
  ZXING4P zxing4p;
  int QRCodeSizeSmall = 180;
  int QRCodeSize = 270;
  int fontSize = 20;
  
  float completeCycleTime = 50;
  float CycleTime = 0;

  //String host = "http://145.126.2.121:8080";
  String host = "http://192.168.0.243:8080";
  
  String QRCodeContent = "https://www.youtube.com/watch?v=KMU0tzLwhbE";
  PImage QRCode;
  PImage QRCodeSmall;
  String sessionID = "";
  String username = "Guest"; 
  String highscoreString = "";
  
  PApplet papplet;
  
  OnlineState onlineState;

  WebClient(PApplet papplet) {
    this.papplet = papplet;
    zxing4p = new ZXING4P();
    onlineState = OnlineState.Connecting;
    getRequest("/plugnpi/api/pi");
  }

  void update(float dt) { //<>//
    CycleTime += dt;
    if (CycleTime > completeCycleTime) {
      CycleTime = 0;
      runRoutineUpdates();
    }
  }
  
  void runRoutineUpdates() {
    switch(onlineState) {
      case Connecting:
        break;
      case QRCode:
        getRequest("/plugnpi/api/pi/link?session=" + sessionID + "&action=request_user");
        break;
      case Ready:
        getRequest("/plugnpi/api/pi/link?session=" + sessionID + "&action=request_join");
        break;
      default:
        break;
    }
    updateHighscores();
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
    case "queued":
      println("Received queued with value \"" + content + "\"\n");
      if(content.equals("true")) {
        println("Calling startGame");
        startGame();
      }
      break;
    default:
      println("Data Type not recognised.\n");
    }
  }

  void updateSessionID(String newSessionID) {
    sessionID = newSessionID;
    String connectURL = host + "/plugnpi/api/pi/link?session=" + sessionID;
    updateQRCode(connectURL);
  }

  void updateQRCode(String content) {
    QRCodeContent = content;
    QRCode = zxing4p.generateQRCode(content, QRCodeSize, QRCodeSize);
    QRCodeSmall = zxing4p.generateQRCode(content, QRCodeSizeSmall, QRCodeSizeSmall);
    println("Set QR code to \"" + content + "\".\n");
  }
  
  void updateHighscores() {
    String HighscoresRaw = getJsonRequest("/plugnpi/api/leaderboard?date=weekly");
    JSONArray highscoresJson = parseJSONArray(HighscoresRaw);
    if(highscoresJson == null) return;
    
    highscoreString = "Weekly highscores:\n\n";
    for(int i = 0; i < highscoresJson.size(); i++) {
      if(i >= 5) break;
      JSONObject highscoreObject = highscoresJson.getJSONObject(i);
      highscoreString += i+1 + ". " + highscoreObject.getString("username") + ": " + parseInt(highscoreObject.getInt("distance")) + "\n";
    }
  }
  
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
    //println("Reponse Content-Length Header: " + post.getHeader("Content-Length"));
    if(post.getContent() != null) {
      receiveData(post.getContent());
    }
  }
  
  PImage getQRCode() {
    return QRCode;
  }
  
  PImage getQRCodeSmall() {
    return QRCodeSmall;
  }
  
  String getQRCodeContent() {
    return QRCodeContent;
  }
  
  String getSessionID() {
    return sessionID;
  }
  
  String getHighscores() {
    return highscoreString;
  }
}
