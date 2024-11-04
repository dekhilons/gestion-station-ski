FROM openjdk:17-alpine

# Install curl
RUN apk add --no-cache curl

# Expose port 8082 instead of 9090, as specified in ENTRYPOINT
EXPOSE 8082

# Download the JAR file directly from Nexus
RUN curl -o stationSki.jar http://admin:sona123456789@172.10.0.140:8081/repository/maven-releases/tn/esprit/stationSki/1.0/stationSki-1.0.jar

# Set the working directory to root
WORKDIR /

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application with the correct Spring profile and server settings
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-Dserver.port=8082", "-Dserver.address=0.0.0.0", "-jar", "stationSki.jar"]
