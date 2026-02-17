# spring-joinfaces-session
showcase spring login session with joinfaces

#uso el jdk 21
export JAVA_HOME=/usr/lib/jvm/jdk-21.0.2 && export MAVEN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED"

#para compilar excluyendo los tests
mvn clean install -DskipTests

#para ejecutar el proyecto springboot
mvn spring-boot:run

