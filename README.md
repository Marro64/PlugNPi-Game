# Plug'n'Pi

***

## Description
This project consists of two parts: The game and the website. The game uses facial tracked motion controls and is accompanied by the website which shows a leaderboard that enables players to have competitive tournaments and keep track of all your scores.

## Requirements

- Raspberry Pi 4 or better with 4GB RAM or higher 
- A second device (eg laptop) to host the website with Tomcat
- 720p or 1080p USB-webcam
- 1080p display with mini-HDMI (adapter possible)
- Mobile Phone with QR scanner
- Text Editor, like IntelliJ IDEA

## Installation
To install the webapp locally, you will need Tomcat and Maven set up on your text editor.

### Setup IntelliJ IDEA
If you don't have Tomcat installed already, follow these steps:
1. Download the latest version of Tomcat [here](https://tomcat.apache.org/). We are using Tomcat 10.1.15.
2. Extract the downloaded Tomcat zip file to your desired location.
3. In your desired text editor, install the plugins `Maven` and `Tomcat and TomEE`.
4. Click `Edit Configurations` in your text editor and add a **Tomcat Local** server
5. Add the directories for your Tomcat install in the two fields and click `OK`.
6. The URL should be *http://localhost:8080/plugnpi/login.html*.
7. In the Deployment tab, click the + and add the plugnpi:war artifact.
8. Click apply and scroll to the bottom, click on the built artifact and ensure both checkboxes are checked.


### Setup Raspberry Pi
1. In the Git, you will find a directory called `Plug_n_Pi`. Copy this directory over to a desired place on your Pi.
2. On the Pi, visit [this website](https://processing.org/download) to download the latest version of Processing 4. The RaspBerry Pi 4 uses 64bit instructions.
3. The following libraries need to be installed and can be installed using Processing's build in `Tools > Tools Manager > Libraries`:
- Minim                            v2.2.2     Damien Di Fede and Anderson Mills
- Video Library for Processing 4   v2.2.2     The Processing Foundation
- OpenCV for Processing            v0.7.0     Greg Borenstein and Florian Bruggisser
- HTTP Requests for Processing     v0.1.5     Rune Madsen, Daniel Shiffman
4. The zxing4p3 library needs to be installed manually by downloading the file from [this website](http://cagewebdev.com/zxing4p/zxing4p3.zip) and needs to be placed inside the `user/home/sketches/libraries` folder.
5. The IP in processing needs to be set to the IP of the server. Get the IP of your server and enter it on line 20 of the file WebClient.
Like this: `String host = "http://[server ip]:8080";`
6. Play the game by opening any of the Processing files and click on the triangular play-button.

## Support
May you encounter any issues during the install, contact us in the [discord channel](https://discord.com/channels/1130758232206495806/1130823228533772288) for inquires. Disclaimer: This channel is only accessible by our group TAs, teachers and the development team.

## Authors and acknowledgment
Created by the Mod5 Goats: Baro, Bas, Kalin, Mauricio, Noah and Marinus.

## Project status
This project is considered finished and is not looking for new owners.