# Користиме базен Docker Image со Java
FROM openjdk:11

# Додаваме Java класата во контекстот на Docker
COPY Main.java /usr/src/app/

# Преминуваме во директориумот каде е класата
WORKDIR /usr/src/app/

# Компајлираме ја Java класата
RUN javac Main.java

# Извршуваме ја Java апликацијата
CMD ["java", "Main"]