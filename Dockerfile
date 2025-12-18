FROM maven:3.8.8-amazoncorretto-21-al2023 as build
WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

FROM amazoncorretto:21.0.5
WORKDIR /app

COPY --from=build ./build/target/*.jar ./libraryapi.jar

EXPOSE 8080
EXPOSE 9090

ENV DATA_SOURCE_URL_PROD=''
ENV DATA_SOURCE_USERNAME_PROD=''
ENV DATA_SOURCE_PASSWORD_PROD=''
ENV GOOGLE_CLIENT_ID=''
ENV GOOGLE_CLIENT_SECRET=''

ENV SPRING_PROFILES_ACTIVE='producao'
ENV TZ='America/Sao_Paulo'

ENTRYPOINT java -jar libraryapi.jar