FROM openjdk:11-jre-slim

ADD target/forexbot.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
