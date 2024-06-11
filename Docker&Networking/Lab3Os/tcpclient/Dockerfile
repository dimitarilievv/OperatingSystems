FROM openjdk:11-jdk
WORKDIR /app
COPY ./src/Client.java /app
RUN javac Client.java
CMD ["java", "Client"]