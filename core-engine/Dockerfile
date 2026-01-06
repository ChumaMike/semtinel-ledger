FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
# We take the JAR that was built by 'mvn clean install' in your root folder
COPY target/core-engine-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]