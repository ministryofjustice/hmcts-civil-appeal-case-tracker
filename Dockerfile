FROM tomcat:8.0.43-jre8

ENV DB_HOST="172.22.5.164" \
    DB_PORT="1433" \
    DB_USER="cact_user" \
    DB_PASS="cact_user" \
    DB_NAME="CACT" \
    ADMIN_USER=admin \
    ADMIN_PASS=admin

ENV CATALINA_OPTS "-Xmx512M -XX:MaxPermSize=1024m"

RUN rm -rf /usr/local/tomcat/webapps/ROOT && mkdir -p /usr/local/tomcat/webapps/listing_calendar

ADD "deploy/CACT.war" /usr/local/tomcat/webapps/ROOT.war
ADD redirect.jsp /usr/local/tomcat/webapps/listing_calendar/index.jsp
