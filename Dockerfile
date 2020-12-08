FROM openjdk:8u201-jdk-alpine3.9
VOLUME /tmp
VOLUME /logs

ENV TZ=Asia/Shanghai
RUN set -eux; \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime; \
    echo $TZ > /etc/timezone
COPY out/artifacts/databee_jar /
EXPOSE 8081

ENTRYPOINT ["java","-jar","/databee.jar","--spring.profiles.active=prod"]
