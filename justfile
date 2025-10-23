build:
    mvn clean package -DskipTests

run:
    mvn spring-boot:run \
        -Dspring-boot.run.profiles=webmvc

up:
    docker compose up --build --detach

down:
    docker compose down