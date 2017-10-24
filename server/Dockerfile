FROM tomcat:8.5-jre8
MAINTAINER twogg-git
ARG ARTIFACTS_DIR
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY /webapp/emailboot.war /usr/local/tomcat/webapps/ROOT.war
