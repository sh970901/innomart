FROM openjdk:17-jdk-slim

ARG DEFAULT_PORT=8080

WORKDIR /mart

COPY target/market-0.0.1-SNAPSHOT.jar ./market-0.0.1-SNAPSHOT.jar
#COPY init_script.sh /innog/init_script.sh
#COPY pinpoint-agent-2.5.3 ./pinpoint-agent-2.5.3
#COPY --from=docker.elastic.co/observability/apm-agent-java:latest /usr/agent/elastic-apm-agent.jar /innog/elastic-apm-agent.jar

ENV PORT $DEFAULT_PORT
ENV key "baobab"

# Expose the required port
EXPOSE $DEFAULT_PORT


#RUN ["bash", "-c", "source /innog/init_script.sh"]
RUN #apt-get update && apt-get install -y vim
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

# Run the application with environment variables
#ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar market-0.0.1-SNAPSHOT.jar"]
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Dkey=baobab -jar market-0.0.1-SNAPSHOT.jar"]
#ENTRYPOINT ["sh", "/innog/init_script.sh", "java", "-jar", "innog.cs-web.webapp.jar"]
# Default CMD (can be overridden by 'docker run' command)
#CMD ["-Xms512m", "-Xmx512m"]


#docker run -e SPRING_PROFILES_ACTIVE=prod my-image
#docker build --build-arg DEFAULT_PORT=80 -t my-image .
#https://docs.docker.com/engine/reference/builder/#arg


#LABEL maintainer="info@javabydeveloper.com"
