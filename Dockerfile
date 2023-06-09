FROM maven:3.8-openjdk-17 as maven
WORKDIR job4j_order
COPY . .
RUN mvn install

FROM openjdk:17.0.2-jdk
WORKDIR job4j_order
COPY --from=maven /job4j_order/target/order.jar app.jar
CMD java -jar app.jar
