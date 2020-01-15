FROM maven:3.6-jdk-13 as build

COPY . /sources
WORKDIR /sources
RUN mvn -B clean verify


FROM openjdk:13-jdk-alpine

COPY --from=build /sources/target/application.jar /project/application.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/project/application.jar"]
