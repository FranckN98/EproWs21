FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
ARG COMMAND_LINE_ARGS
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]