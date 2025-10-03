FROM tomcat:9.0.107-jdk8-temurin-jammy

ENV DB_HOST="172.22.5.164" \
    DB_PORT="1433" \
    DB_USER="cact_user" \
    DB_PASSWORD="cact_user" \
    DB_NAME="CACT" \
    ADMIN_USER=admin \
    ADMIN_PASS=admin

#This is no set in deployment.yaml to avoid the base image overwriting what we set in the dockerfile
#ENV CATALINA_OPTS "-Xms2048m -Xmx2048m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/usr/local/tomcat/logs/gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=10M"

RUN mkdir -p /opt
RUN rm -rf /usr/local/tomcat/webapps/ROOT && rm -rf /usr/local/tomcat/webapps/docs && rm -rf /usr/local/tomcat/webapps/examples

ADD "deploy/CACT.war" /usr/local/tomcat/webapps/ROOT.war
ADD context.xml /usr/local/tomcat/conf/context.xml

RUN adduser --disabled-password tomcat -u 1001 && chown -R tomcat:tomcat /usr/local/tomcat
USER 1001
