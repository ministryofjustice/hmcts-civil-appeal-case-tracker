FROM tomcat:8.5.73-jdk8-openjdk

ENV DB_HOST="172.22.5.164" \
    DB_PORT="1433" \
    DB_USER="cact_user" \
    DB_PASSWORD="cact_user" \
    DB_NAME="CACT" \
    ADMIN_USER=admin \
    ADMIN_PASS=admin

ENV CATALINA_OPTS "-Xmx512M -XX:MaxPermSize=1024m"

RUN mkdir -p /opt
RUN rm -rf /usr/local/tomcat/webapps/ROOT && rm -rf /usr/local/tomcat/webapps/docs && rm -rf /usr/local/tomcat/webapps/examples

ADD "deploy/CACT.war" /usr/local/tomcat/webapps/ROOT.war
ADD context.xml /usr/local/tomcat/conf/context.xml

RUN adduser --disabled-password tomcat -u 1001 && chown -R tomcat:tomcat /usr/local/tomcat
USER 1001
