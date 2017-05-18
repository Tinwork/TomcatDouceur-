# Tinwork tomcat

# Project 

This project is a shorten URL J2EE app made as a project given within the Java course. So what the purpose ? 
Well, it shorten URL and save it into the database with several options such as :

This project is a shorten URL webapp made using Java's web technology J2EE. This project is a work given during the Java Course. Two version will be released. This first one using no framework, only Plain Java Classes, Object, Interfaces...) 
This shorten URL app give you the following possibilites: 

* Adding a single to a multiple password to your short url
* Make it private by restricting the access to an email (Multi email support will be analysis)
* Restrict during an amount of time 
* Verify by a captcha 
* For user's URL you'll get statistics of the popularity of your shortern URL

# Install 

In order to launch the project you will need to use Docker. Otherwise i suggest you to download docker for your version by clicking on the next link (Download docker)[https://www.docker.com/community-edition]. After installing docker follow the instructions below 

* Clone this repository and go in there :) 
* docker-compose up (This will create 3 containers which will be controlled by docker-compose)
* Access the tomcat project using localhost:5000

Accessing to the database using mysql command
* Use docker exec -ti mysql_container_id /bin/bash 

Accessing to the database using phpmyadmin
* Access to phpmyadmin using localhost:8080
* Use docker inspect mysql_container_id and look for the **IP_ADRESS** 
* Enter the container IP and the logs 

Et voilà ! 

# Code 

* JDK >= 8
* We use Intellij IDEA to help us with the development
* Download the JDBC driver and the Tomcat driver 
* Use maven to import every libs which are used by the project
