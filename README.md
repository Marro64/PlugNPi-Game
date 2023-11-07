# cs23-26-main



## Getting started

To make it easy for you to get started with GitLab, here's a list of recommended next steps.

Already a pro? Just edit this README.md and make it your own. Want to make it easy? [Use the template at the bottom](#editing-this-readme)!

## Add your files

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin https://gitlab.utwente.nl/computer-systems-project/2023-2024/student-projects/cs23-26/cs23-26-main.git
git branch -M main
git push -uf origin main
```

## Integrate with your tools

- [ ] [Set up project integrations](https://gitlab.utwente.nl/computer-systems-project/2023-2024/student-projects/cs23-26/cs23-26-main/-/settings/integrations)

## Collaborate with your team

- [ ] [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ] [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ] [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ] [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ] [Set auto-merge](https://docs.gitlab.com/ee/user/project/merge_requests/merge_when_pipeline_succeeds.html)

## Test and Deploy

Use the built-in continuous integration in GitLab.

- [ ] [Get started with GitLab CI/CD](https://docs.gitlab.com/ee/ci/quick_start/index.html)
- [ ] [Analyze your code for known vulnerabilities with Static Application Security Testing(SAST)](https://docs.gitlab.com/ee/user/application_security/sast/)
- [ ] [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/ee/topics/autodevops/requirements.html)
- [ ] [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/ee/user/clusters/agent/)
- [ ] [Set up protected environments](https://docs.gitlab.com/ee/ci/environments/protected_environments.html)

***

## Name
Plug'n'Pi

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
5. Play the game by opening any of the Processing files and click on the triangular play-button.


## Usage
Use examples liberally, and show the expected output if you can. It's helpful to have inline the smallest example of usage that you can demonstrate, while providing links to more sophisticated examples if they are too long to reasonably include in the README.

## Support
May you encounter any issues during the install, contact us in the [discord channel](https://discord.com/channels/1130758232206495806/1130823228533772288) for inquires. Disclaimer: This channel is only accessible by our group TAs, teachers and the development team.

## Contributing
State if you are open to contributions and what your requirements are for accepting them.

For people who want to make changes to your project, it's helpful to have some documentation on how to get started. Perhaps there is a script that they should run or some environment variables that they need to set. Make these steps explicit. These instructions could also be useful to your future self.

You can also document commands to lint the code or run tests. These steps help to ensure high code quality and reduce the likelihood that the changes inadvertently break something. Having instructions for running tests is especially helpful if it requires external setup, such as starting a Selenium server for testing in a browser.

## Authors and acknowledgment
Show your appreciation to those who have contributed to the project.

## License
For open source projects, say how it is licensed.

## Project status
If you have run out of energy or time for your project, put a note at the top of the README saying that development has slowed down or stopped completely. Someone may choose to fork your project or volunteer to step in as a maintainer or owner, allowing your project to keep going. You can also make an explicit request for maintainers.
