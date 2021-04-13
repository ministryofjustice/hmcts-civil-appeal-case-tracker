FROM tomcat:8.5.65-jdk8-openjdk

ENV DB_HOST="172.22.5.164" \
    DB_PORT="1433" \
    DB_USER="cact_user" \
    DB_PASSWORD="cact_user" \
    DB_NAME="CACT" \
    ADMIN_USER=admin \
    ADMIN_PASS=admin

ENV CATALINA_OPTS "-Xmx512M -XX:MaxPermSize=1024m"

RUN rm -rf /usr/local/tomcat/webapps/ROOT && rm -rf /usr/local/tomcat/webapps/docs

ADD "deploy/CACT.war" /usr/local/tomcat/webapps/ROOT.war
ADD context.xml /usr/local/tomcat/conf/context.xml
