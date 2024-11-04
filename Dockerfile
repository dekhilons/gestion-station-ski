FROM openjdk:17-alpine
RUN apk add --no-cache curl
EXPOSE 9090
RUN curl -o app.jar http://admin:sona123456789@172.10.0.140:8081/repository/maven-releases/tn/esprit/stationSki/1.0/stationSki-1.0.jar
WORKDIR /
ENV SPRINGPROFILES=prod
ENTRYPOINT ["java", "-jar", "app.jar","-Dspring.profiles.active=${SPRINGPROFILES}","-jar", "-Dserver.port=8082", "-Dserver.address=0.0.0.0"]


