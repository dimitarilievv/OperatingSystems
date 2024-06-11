FROM openjdk:11-jdk
WORKDIR /app
COPY ./src/SoundLevelMonitor.java /app
RUN javac SoundLevelMonitor.java
CMD ["java", "SoundLevelMonitor"]