### Tinwork TOMCAT (without Hibernate)

### Description:

Tinwork tomcat is a Java web app which allow you to create short link. The code is avaible by clicking on this link : [Code](https://github.com/Tinwork/TomcatDouceur-)

### Functionnality

- Shorting url 
- Password / Multiple password
- Timeout
- Maximum use
- Email verification 
- Captcha verification
- Dashboard

### Technology uses in this version

- Tomcat
- JEE
- JDBC mysql driver
- JWT
- Chart.js (rendering chart)
- Bootstrap
- Docker

### Launch the project

/!\ This project required to be used with Docker. 
For windows please install the following questions: [Install docker](https://www.docker.com/products/docker-toolbox)

/!\ For windows user **be careful to note the IP ADRESS of the container**

* Before starting the docker engine we need to update sompe parameters
* In the docker-compose.yml file please modify the following section

```yml
environment:
       GMAIL_USER: <Your address mail>
       GMAIL_PASSWORD: <Your adress password>
```

* This modification allow you to be able to send mails (we use an external smtp server)


```shell

// First go to the root of the project
cd tinworktomcat

// Run docker
docker-compose up -d 

```
* The project is now available at **http://localhost:5000/tinwork/home**
* If you encounter any error with Docker, please contact the administrator at: marc.inthaamnouay@gmail.com

### Note for window user

* For windows user you need to add the **docker adress IP** in your hosts
* Add the IP address in your **hosts** like below

```shell
local.dev     <IP adress>  
```

* The project should then be accessible by using this adresses **http://local.dev:5000/tinwork/home**


### Debugging

* For debugging the project you need to locate the **container id** of the container name **tinwork-tomcat** using the command **docker ps**
* Follow these steps below: 

```shell
// Accessing to the container
docker exec -ti <CONTAINER ID ID: eg: 009fbbc40606> /bin/bash 

// Navigate to the logs file
cd logs

// Listen to the logs
xtail .
```

### Debugging the database

* Database is available at the following address: **http://localhost:8080** 

```shell
database: tomcat-db
username: root
password: tinwork
```


### Misc

* If you encounter any issue related to the project, please contact **marc.inthaamnouay@gmail.com**
* IF you can't start the project, then please use this following link [Tinwork tomcat production version](http://ec2-52-50-169-150.eu-west-1.compute.amazonaws.com:8080/tinwork/home)
* A user already exist with the following credentials 

```shell

username: foo
pwd     : foo
```

# Maintainers

* Marc Intha-amnouay
* Antoine Renault
* Didier Youn 
