FROM openjdk:20
EXPOSE 12345

ADD target/ClusteredData-Warehouse-0.0.1-SNAPSHOT.jar ClusteredData-Warehouse-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "ClusteredData-Warehouse-0.0.1-SNAPSHOT.jar"]