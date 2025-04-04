FROM amazoncorretto:21 as builder

# try patching system for security fixes
RUN yum update -y && yum clean all


#Copy project config
COPY gradle /usr/src/nnddataexchange/gradle
COPY gradlew /usr/src/nnddataexchange/gradlew
COPY build.gradle /usr/src/nnddataexchange/build.gradle
COPY settings.gradle /usr/src/nnddataexchange/settings.gradle

#Copy sources
COPY nnd-data-exchange-service /usr/src/nnddataexchange/nnd-data-exchange-service

COPY . /usr/src/nnd

WORKDIR /usr/src/nnd

#Build report service along with any required libraries
RUN ./gradlew :nnd-data-exchange-service:buildNeeded -x test --no-daemon

FROM amazoncorretto:21
# try patching system for security fixes
RUN yum update -y && yum clean all

COPY --from=builder /usr/src/nnd/nnd-data-exchange-service/build/libs/nnd-data-exchange-service*.jar nnd-data-exchange-service.jar

# Run jar
ENTRYPOINT ["java", "-Xms256m", "-Xmx2048m", "-jar", "nnd-data-exchange-service.jar"]
CMD ["java", "-Xms256m", "-Xmx2048m", "-jar", "nnd-data-exchange-service.jar"]
