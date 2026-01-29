FROM amazoncorretto:25 as builder
# try patching system for security fixes
RUN yum update -y && yum clean all

#Copy project config
COPY gradle /usr/src/nndservice/gradle
COPY gradlew /usr/src/nndservice/gradlew
COPY build.gradle /usr/src/nndservice/build.gradle
COPY settings.gradle /usr/src/nndservice/settings.gradle

#Copy sources
COPY nnd-service /usr/src/nndservice/nnd-service

COPY . /usr/src/nnd

WORKDIR /usr/src/nnd

#Build report service along with any required libraries
RUN ./gradlew :nnd-service:buildNeeded -x test --no-daemon

FROM amazoncorretto:25
# try patching system for security fixes
RUN yum update -y && yum clean all

COPY --from=builder /usr/src/nnd/nnd-service/build/libs/nnd-service*.jar nnd-service.jar

# JVM picks up JAVA_TOOL_OPTIONS from HELM
ENTRYPOINT ["java", "-jar", "nnd-service.jar"]