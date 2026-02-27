FROM tomcat:9.0.107-jdk11-temurin-jammy

#This is now set in deployment.yaml to avoid the base image overwriting what we set in the dockerfile
#ENV CATALINA_OPTS "-Xms2048m -Xmx2048m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/usr/local/tomcat/logs/gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=10M"

#Add main war file to tomcat server as ROOT.war and add the necessary config files
USER root

RUN adduser --disabled-password tomcat -u 1001

ADD "deploy/CACT.war" /usr/local/tomcat/webapps/ROOT.war
ADD context.xml /usr/local/tomcat/conf/context.xml

# Remove default index files
RUN rm -rf /usr/local/tomcat/webapps/{ROOT,examples,host-manager,manager,docs}

# Copy snippet.xml to /tmp
COPY deploy_assets/error-snippet.xml /tmp/error-snippet.xml

# Modify web.xml to insert the snippet before </web-app>
RUN sed -i "/<\/web-app>/i $(cat /tmp/error-snippet.xml)" $CATALINA_HOME/conf/web.xml && \
    rm /tmp/error-snippet.xml

# Copy error.jsp to webapps/ROOT
RUN mkdir -p $CATALINA_HOME/webapps/ROOT
COPY deploy_assets/error.jsp $CATALINA_HOME/webapps/ROOT/error.jsp

RUN chown -R 1001:1001 /usr/local/tomcat

USER 1001

CMD ["catalina.sh", "run"]
