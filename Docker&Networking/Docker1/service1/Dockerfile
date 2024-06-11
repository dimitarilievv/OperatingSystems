FROM openjdk:11-jdk
WORKDIR /app
COPY ./src/SoundLevelSensor.java /app
RUN javac SoundLevelSensor.java
CMD ["java", "SoundLevelSensor"]