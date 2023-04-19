FROM openjdk:17

WORKDIR /app

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar
COPY wait-for-it.sh /app/wait-for-it.sh

CMD ["java", "-jar", "app.jar"]