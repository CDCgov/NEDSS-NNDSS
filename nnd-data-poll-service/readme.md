gradle clean
gradle bootJar
gradle clean bootJar
java -jar build/libs/myapp.jar

COMMAND
java -Dspring.profiles.active=dev -Dcustom.property=myValue -jar build/libs/nnd-data-poll.jar


LINUX
export SPRING_PROFILES_ACTIVE=dev
export CUSTOM_PROPERTY=myValue
java -jar build/libs/nnd-data-poll.jar



WINS
set SPRING_PROFILES_ACTIVE=dev
set CUSTOM_PROPERTY=myValue
java -jar build/libs/nnd-data-poll.jar


MAC
export SPRING_PROFILES_ACTIVE=dev
export CUSTOM_PROPERTY=myValue
java -jar build/libs/nnd-data-poll.jar

Ex: Setting up the scheduler time.
"*/1 * * * * ?" => For 1 second
"0 */1 * * * ?" => For 1 minute
"0 0 */1 * * ?" => For 1 hour
"0 0 0 */1 * ?" => For every day
"0 0 0 1 1 *" => It scheduled as 1 date of 1st month(january) every year