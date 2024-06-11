FROM openjdk:11-jdk
WORKDIR /app
COPY ./src/UDPServer.java /app
RUN javac UDPServer.java
CMD ["java","UDPServer"]