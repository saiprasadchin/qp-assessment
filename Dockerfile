FROM maven:3.8.3-openjdk-17 AS build
COPY src c/dev/grocery-application/src
COPY pom.xml c/dev/grocery-application
RUN mvn -f c/dev/grocery-application/pom.xml clean package -DskipTests
EXPOSE 8080
ENTRYPOINT ["java","-jar","c/dev/grocery-application/target/grocery-application-0.0.1-SNAPSHOT.jar"]