# The os that we're going to be based off 
FROM debian:latest

# Update the sources list 
RUN echo "deb http://ftp.de.debian.org/debian jessie-backports main" >> /etc/apt/sources.list

# Install the dependencies and the lib that need tomcat 
RUN apt-get update && apt-get upgrade -y && \
    apt-get install -y \
    -t jessie-backports  openjdk-8-jre-headless ca-certificates-java \
    openjdk-8-jdk \
    nano \
    wget 

RUN which java

# Change working directory 
WORKDIR "/opt"

# Download tomcat, extract and rename the folder 
RUN wget http://mirrors.standaloneinstaller.com/apache/tomcat/tomcat-9/v9.0.0.M19/bin/apache-tomcat-9.0.0.M19.tar.gz && \
    tar xvf apache-tomcat-9.0.0.M19.tar.gz 

# Rename the folder
RUN mv apache-tomcat-9.0.0.M19 tomcat9

# Change the workdir 
WORKDIR "tomcat9"

# Export env variable 
RUN echo "export CATALINA_HOME='/opt/tomcat9'" >> /etc/environment && \
    echo "export JAVA_HOME='/usr/lib/jvm/java-8-openjdk'" >> /etc/environment && \
    echo "export JRE_HOME='/usr/lib/jvm/java-8-openjdk/jre'" >> /etc/environment && \
    . ~/.bashrc

# Add user to tomcat 
RUN echo "<role rolename='manager-gui' />\n<user username='root' password='root' roles='manager-gui'>\n<role rolename='admin-gui' />\n<user username='admin' password='admin' roles='admin-gui,manager-gui'>" >> ./conf/tomcat-users.xml

# Change the server reload config 
RUN sed -i.bak 's/<Context>/<Context reloadable="true">/g' conf/context.xml

# Add a log files
RUN touch logs/apps.log

# Run tomcat 
CMD ./bin/catalina.sh run
