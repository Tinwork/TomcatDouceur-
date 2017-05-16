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
RUN mv apache-tomcat-9.0.0.M19 tomcat9 && \
    rm apache-tomcat-9.0.0.M19.tar.gz

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

# Add the JDBC driver into the libs folder
RUN wget https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.41.tar.gz && \
    tar -xvf mysql-connector-java-5.1.41.tar.gz && \
    cp mysql-connector-java-5.1.41/mysql-connector-java-5.1.41-bin.jar /opt/tomcat9/lib/ && \
    rm mysql-connector-java-5.1.41.tar.gz

# Add the Java-jwt dependency to tomcat
RUN wget http://central.maven.org/maven2/com/auth0/java-jwt/3.2.0/java-jwt-3.2.0.jar && \
    cp java-jwt-3.2.0.jar /opt/tomcat9/lib/ && \
    rm java-jwt-3.2.0.jar

# Add the Java jackson core to tomcat
RUN wget http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.8.8/jackson-core-2.8.8.jar && \
    cp jackson-core-2.8.8.jar /opt/tomcat9/lib/ && \
    rm jackson-core-2.8.8.jar

# Add the Java jackson databind
RUN wget http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.8.8/jackson-databind-2.8.8.jar && \
    cp jackson-databind-2.8.8.jar /opt/tomcat9/lib/ && \
    rm jackson-databind-2.8.8.jar

# Add the Java jackson annotation
RUN wget http://repo2.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.8.0/jackson-annotations-2.8.0.jar && \
    cp jackson-annotations-2.8.0.jar /opt/tomcat9/lib && \
    rm jackson-annotations-2.8.0.jar

# Add the apache stuff
RUN wget http://central.maven.org/maven2/commons-codec/commons-codec/1.9/commons-codec-1.9.jar && \
    cp commons-codec-1.9.jar /opt/tomcat9/lib && \
    rm commons-codec-1.9.jar

# Add apache validator
RUN wget http://central.maven.org/maven2/commons-validator/commons-validator/1.6/commons-validator-1.6.jar && \
    cp commons-validator-1.6.jar /opt/tomcat9/lib && \
    rm commons-validator-1.6.jar

# Run tomcat 
CMD ./bin/catalina.sh run
