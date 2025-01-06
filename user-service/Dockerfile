FROM maven:3.8.3-openjdk-17
WORKDIR /app

#copying only required folders for maven dep installation
COPY .mvn/ .mvn
COPY mvnw pom.xml .

#does not execute build or compile the code or copies local artifact into local .m2 folder (unlike mvn install)
#It only downloads dependencies declared in pom.xml and prepares Maven to work offline
RUN ./mvnw dependency:go-offline

#now copying the code folder
COPY src ./src

CMD ["./mvnw" , "spring-boot:run"]


