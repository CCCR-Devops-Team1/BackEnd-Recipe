FROM openjdk:17
RUN mkdir /app
WORKDIR /app
COPY ./build/libs/recipe_project-0.0.1-SNAPSHOT.jar ./

CMD ["java","-jar","-Dspring.profiles.active=dev /app/recipe_project-0.0.1-SNAPSHOT.jar"]