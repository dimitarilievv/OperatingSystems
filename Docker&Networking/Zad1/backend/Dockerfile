FROM openjdk:11-jdk
WORKDIR /app
COPY SimpleHttpServer.java /app
RUN javac SimpleHttpServer.java
CMD ["java", "SimpleHttpServer"]