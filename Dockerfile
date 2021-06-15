FROM node:16-alpine AS frontend_build

WORKDIR client
# Caching layers
COPY client/*.json /client/
RUN npm install

# Build
COPY client/src /client/src
COPY client/public /client/public
RUN npm run build

FROM adoptopenjdk/openjdk11:jdk-11.0.11_9-alpine AS backend_build

WORKDIR server
# Caching layers
COPY server/pom.xml /server/
COPY server/mvnw /server/
COPY server/.mvn /server/.mvn
RUN sh mvnw initialize -DskipTests
RUN sh mvnw clean -DskipTests
RUN sh mvnw dependency:go-offline -DskipTests

# Build
COPY server/src /server/src
RUN ls -lh && sh mvnw clean install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:11-jre
VOLUME /tmp
COPY --from=frontend_build /client/build /app/public
ARG DEPENDENCY=/server/target/dependency
COPY --from=backend_build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=backend_build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=backend_build ${DEPENDENCY}/BOOT-INF/classes /app
RUN ls -lhtr  /app/public/
EXPOSE 8080
ENTRYPOINT ["java","-cp","app:app/lib/*","dmo.server.Main"]

