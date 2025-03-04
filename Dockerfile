FROM maven:3.8.4-openjdk-17 AS base
WORKDIR /app 
COPY ./pom.xml /app
RUN mvn dependency:go-offline -B
COPY ./src /app/src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
RUN mkdir /opt/app
EXPOSE 8080
COPY --from=base /app/target/Rest_Api_Forum-0.0.1-SNAPSHOT.war /opt/app/app.war
CMD ["java", "-jar", "/opt/app/app.war"]

