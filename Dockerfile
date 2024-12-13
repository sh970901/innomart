FROM openjdk:17-alpine

WORKDIR /mart

COPY target/market-0.0.1-SNAPSHOT.jar ./market-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["sh", "-c", "java -Dkey=baobab -Dspring.profiles.active=dev -jar market-0.0.1-SNAPSHOT.jar"]
