FROM openjdk:8
RUN apt-get update && apt-get install -y maven
COPY . ./pimodbus
RUN mvn -f /pimodbus/pom.xml -Dmaven.test.skip=true clean install
EXPOSE 8080
#CMD ["java", "-jar", "/pimodbus/target/pimodbusmaster-0.0.1-SNAPSHOT.war"] 