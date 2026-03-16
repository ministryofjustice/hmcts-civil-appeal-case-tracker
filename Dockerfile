FROM tomcat:9.0.107-jdk11-temurin-jammy

ENV CATALINA_OPTS="-Xmx512M -XX:MaxMetaspaceSize=256m"

RUN rm -rf  /usr/local/tomcat/webapps/{ROOT,examples,host-manager,manager,docs} \
    && mkdir -p /file/

COPY --chown=1001:1001 "deploy/CACT.war" /usr/local/tomcat/webapps/ROOT.war
COPY --chown=1001:1001 context.xml /usr/local/tomcat/conf/context.xml

# Modify web.xml to insert the error code config before </web-app>
COPY deploy_assets/error-snippet.xml /tmp/error-snippet.xml

#Temporary copy to llow Bing to authorise a reindex of a cached page
COPY --chown=1001:1001 deploy_assets/BingSiteAuth.xml /usr/local/tomcat/webapps/ROOT/BingSiteAuth.xml

RUN sed -i "/<\/web-app>/i $(cat /tmp/error-snippet.xml)" $CATALINA_HOME/conf/web.xml && \
    rm /tmp/error-snippet.xml

RUN adduser --disabled-password tomcat -u 1001 && chown -R tomcat:tomcat /usr/local/tomcat

USER 1001
