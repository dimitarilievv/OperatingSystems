FROM openjdk:11-jdk
WORKDIR /app
COPY ./src/Server.java /app
RUN javac Server.java
CMD ["java", "Server"]