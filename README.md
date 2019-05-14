# How to make it work

REST API is working with database which is currently set as local MySQL. So for making API works just change setting inside Resources/applications.properties file.
You need to change url and credentials for login to database then everything should work. If you use some other database you need to change also other setting inside this file
and of course add new dependencies for adapter.

API is Spring boot app so no need to deploy on some local installed server, IDEA will manage such work for you. 

Step 1. Set database for REST API.

Step 2. Compile and start REST API server on local Tomcat. You just need to press start button IDEA will do everything for you. (IntelliJ IDEA)

Step 3. Compile and start mobile app in emulator mode. (Android Studio)

If somehow mobile app cannot connect to the server there is some problem with url inside mobile app which should be set to local server
for android that is http://10.0.2.2:8080/mobile-app-ws/. 8080 is port for Tomcat maybe you will have another port. In that case you need to change this url inside activities/LoginActivity and also inside network/ApiClient .
