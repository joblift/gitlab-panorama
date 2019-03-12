FROM maven:3.6.0-jdk-8 as build

COPY . /sources
WORKDIR /sources
RUN mvn clean verify 


FROM openjdk:8-jdk-alpine

COPY --from=build /sources/target/application.jar /project/application.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/project/application.jar"]
