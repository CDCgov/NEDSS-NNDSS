FROM amazoncorretto:17 as builder


#Copy project config
COPY gradle /usr/src/nnd/gradle
COPY gradlew /usr/src/nnd/gradlew
COPY build.gradle /usr/src/nnd/build.gradle
COPY settings.gradle /usr/src/nnd/settings.gradle

#Copy sources
COPY nnd-data-exchange-service /usr/src/nnd/nnd-data-exchange-service

COPY . /usr/src/nnd

WORKDIR /usr/src/nnd

#Build report service along with any required libraries
RUN ./gradlew :nnd-data-exchange-service :buildNeeded -x test --no-daemon

FROM amazoncorretto:17

COPY --from=builder /usr/src/nnd/nnd-data-exchange-service/build/libs/nnd-data-exchange-service*.jar nnd-data-exchange-service.jar

# Run jar
ENTRYPOINT ["java", "-jar", "nnd-data-exchange-service.jar"]
CMD ["java", "-jar", "nnd-data-exchange-service.jar"]
