FROM openjdk:17-alpine
RUN apk add --no-cache curl
EXPOSE 9090
RUN curl -o app.jar http://admin:nexus@192.168.33.10:8082/repository/maven-releases/tn/esprit/spring/stationSki/1.0/stationSki-1.0.jar
WORKDIR /
ENV SPRINGPROFILES=prod
ENTRYPOINT ["java", "-jar", "app.jar","-Dspring.profiles.active=${SPRINGPROFILES}","-jar", "-Dserver.port=8083", "-Dserver.address=0.0.0.0"]
