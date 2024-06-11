FROM nginx
WORKDIR /app
COPY ./src/Main.java /app
RUN javac Main.java
CMD ["java", "Main"]