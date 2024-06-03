FROM openjdk:17 

WORKDIR /usr/app/src

COPY target/expense-manager-0.0.1-SNAPSHOT.jar /usr/app/src/

EXPOSE 8080

CMD [ "java", "-jar", "expense-manager-0.0.1-SNAPSHOT.jar"]