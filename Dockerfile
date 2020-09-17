FROM openjdk:8
COPY ./cache-api/target/cache-api-0.0.1.jar cache-api-0.0.1.jar
CMD ["java","-jar","cache-api-0.0.1.jar"]