FROM openjdk:21-jdk
COPY target/lib /usr/src/lib
COPY target/account-service-1.0.0-runner.jar /usr/src/
WORKDIR /usr/src/
CMD java -Xmx64m -jar account-service-1.0.0-runner.jar
