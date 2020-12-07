FROM openjdk:8u201-jdk-alpine3.9
VOLUME /tmp
VOLUME /logs
#ADD target/databee-1.0.jar app.jar
COPY out/artifacts/databee_jar /
EXPOSE 8081

ENTRYPOINT ["java","-jar","/databee.jar","--spring.profiles.active=prod"]
