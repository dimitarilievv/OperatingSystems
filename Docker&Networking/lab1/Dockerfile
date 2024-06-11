FROM debian:11-slim

RUN apt update && apt install -y nginx && apt clean

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]