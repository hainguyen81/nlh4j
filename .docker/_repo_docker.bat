@echo off

docker volume create --name nlh4j-repo
docker run -it -v nlh4j-repo:/root/.m2/repository maven mvn archetype:generate # will download artifacts
docker run -it -v nlh4j-repo:/root/.m2/repository maven mvn archetype:generate # will reuse downloaded artifacts