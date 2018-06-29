FROM images/alpine-java
EXPOSE 8116
COPY ./target/mslave-service.jar /sxdata/mslave-service/
WORKDIR /sxdata/mslave-service/
CMD ["java", "-jar", "mslave-service.jar"]