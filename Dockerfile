FROM openjdk:17
RUN mkdir /app
WORKDIR /app
COPY ./build/libs/recipe_project-0.0.1-SNAPSHOT.jar ./
RUN ["sudo","yum","install","-y","gcc"]
RUN ["sudo","wget","http://download.redis.io/redis-stable.tar.gz && tar xvzf redis-stable.tar.gz && cd redis-stable && make"]
RUN ["sudo","cp","src/redis-cli","/usr/bin/"]
CMD ["java","-jar","-Dspring.profiles.active=dev","recipe_project-0.0.1-SNAPSHOT.jar"]