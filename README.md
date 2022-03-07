# TAB2XML EECS 2311 Software Design Project (Winter 2022)

[How to Import project to eclipse via Github](https://github.com/csardana/TAB2XML#How-to-Import-project-to-eclipse-via-Github) </br>
[Setting up Gradle and Running the Application](https://github.com/csardana/TAB2XML#Setting-up-Gradle-and-Running-the-Application)</br>
[How to create a Personal Access Token](https://github.com/csardana/TAB2XML#How-to-create-a-Personal-Access-Token)
## Documentations
[Requirments Document](Documentation/Requirments-.pdf)</br>
[User Manual](Documentation/User-Manual.pdf)</br> 
[Testing Document](Documentation/Testing_Document.pdf) 


## How to Import project to eclipse via Github
1. Open eclipse and create a new workspace 
<img src="Documentation/Pictures/um1.png" width="500">

2. After selecting launch, select File -> Import
<img src="Documentation/Pictures/um2.png" width="300">

3. Now choose Git -> Projects from Git (with smart import) and then click next. 
<img src="Documentation/Pictures/um3.png" width="500">

4. Select Clone URI
<img src="Documentation/Pictures/um4.png" width="500">

5.Now under the Location box, in the URI textbox copy paste our Github reposirtory which would have all source code: https://github.com/csardana/TAB2XML. For the Autentication box, in the textfield for user: enter your github username and for the password: enter the Personal Access Token.
Now when this is all done, and next is clicked, you will select next again in the next window and then click finish when prompted. </br>
Note: If you don't have a Personal Access Token, Please look at our section [How to create a Personal Access Token](https://github.com/csardana/TAB2XML#How-create-to-a-Personal-Access-Token) which will help you procced further in the step of this installation. </br>
<img src="Documentation/Pictures/um5.png" width="500">

### Setting up Gradle and Running the Application
1. Open Preferences -> Select Gradle -> Specific Gradle version -> Apply and close. 
<img src="Documentation/Pictures/um7.png" width="500">

2. To get gradle tasks so the application can run, "Run -> Show View -> Other" 
<img src="Documentation/Pictures/um8.png" width="300">

3. Now in the show view select "Gradle -> Gradle Tasks" 
<img src="Documentation/Pictures/um9.png" width="300">

4. After doing these steps, there would be a window named Gradle tasks and underneath that the project name "Project_name -> application -> run" </br>
Note: Double click "run" </br>
<img src="Documentation/Pictures/um10.png" width="500">

5. Finally the application will open up and the following will open waiting for your input. </br>
<img src="Documentation/Pictures/um11.png" width="500">

## How to create a Personal Access Token
This Personal Access Token will be used when importing our project into eclipse via Github. This portion will take you step by step in creating the Personal Access Token. 
1. Go to your github settings </br>
![](Documentation/Pictures/generate_token1.png)

2. Scroll down and select "Developer settings" in the side menu bar.</br>
![](Documentation/Pictures/generate_token2.png)

3. Now from these three options select "Personal access tokens"</br>
<img src="Documentation/Pictures/generate_token3.png" width="300">

4. To generate a new token, select "Generate new token" </br>
<img src="Documentation/Pictures/generate_token4.png" width="500">

5. Now it will prompt you to enter your password for verification, after doing that, select a date for how long you want this token to work for and make a note for what this token is for. Then scroll all the way at the bottom to click generate. </br>
Note: When the token is generated, copy and paste it somewhere (e.g notes or notpad) so you can enter when importing a project from github. 
<img src="Documentation/Pictures/generate_token5.png" width="500">
Note: You will see many other options, but we will not select any of those and just select "Generate key" at the bottom of the page!!


   
# Licences
[![CC BY-SA 4.0][cc-by-sa-shield]][cc-by-sa]

This work is licensed under a
[Creative Commons Attribution-ShareAlike 4.0 International License][cc-by-sa].

[![CC BY-SA 4.0][cc-by-sa-image]][cc-by-sa]

[cc-by-sa]: http://creativecommons.org/licenses/by-sa/4.0/
[cc-by-sa-image]: https://licensebuttons.net/l/by-sa/4.0/88x31.png
[cc-by-sa-shield]: https://img.shields.io/badge/License-CC%20BY--SA%204.0-lightgrey.svg


