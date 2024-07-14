FROM ubuntu:22.04
WORKDIR /app
COPY . .

# Install dependencies
RUN chmod +x ./docker/scripts/setup.sh
RUN ./docker/scripts/setup.sh

RUN java -version
RUN lein -version

# Compile jar
RUN lein uberjar

EXPOSE 80

CMD ["java", "-jar", "target/uberjar/fullstackclj-0.1.0-SNAPSHOT-standalone.jar"]