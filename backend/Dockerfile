# =========================
# BUILD
# =========================

FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN chmod +x mvnw

# Baixa as dependências primeiro para aproveitar o cache do Docker
RUN ./mvnw dependency:go-offline

COPY src src

# Também executa os testes durante o build
RUN ./mvnw clean package


# =========================
# RUNTIME
# =========================

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]