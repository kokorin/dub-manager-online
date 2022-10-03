FROM adoptopenjdk/eclipse-temurin:17-alpine AS build

WORKDIR server
# Caching layers
COPY pom.xml /server/
COPY mvnw /server/
COPY .mvn /server/.mvn
RUN sh mvnw initialize -DskipTests
RUN sh mvnw clean -DskipTests
RUN sh mvnw dependency:go-offline -DskipTests

# Build
COPY src /server/src
RUN sh mvnw clean install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM adoptopenjdk/openjdk11:alpine-jre
ARG DEPENDENCY=/server/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

EXPOSE 8080
ENTRYPOINT ["java","-cp","app:app/lib/*","dmo.server.Main"]

