FROM openjdk:8
COPY pimodbusmaster-0.0.1-SNAPSHOT.war /pimodbusmaster-0.0.1-SNAPSHOT.war
EXPOSE 8080
CMD ["java", "-jar", "./pimodbusmaster-0.0.1-SNAPSHOT.war"] 