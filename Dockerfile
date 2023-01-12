#
# Build stage
#
FROM maven:3.8.4-openjdk-17 AS build
COPY src /home/TaskProcessor/src
COPY pom.xml /home/TaskProcessor
RUN mvn -f /home/TaskProcessor/pom.xml clean package

#
# Package stage
#
FROM maven:3.8.4-openjdk-17
COPY --from=build /home/TaskProcessor/target/TaskProcessor-0.0.1-SNAPSHOT.jar /usr/local/lib/TaskProcessor-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/TaskProcessor-0.0.1-SNAPSHOT.jar"]