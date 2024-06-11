FROM openjdk:11-jdk
WORKDIR /app
COPY ./src/UDPClient.java /app
RUN javac UDPClient.java
CMD ["java","UDPClient"]