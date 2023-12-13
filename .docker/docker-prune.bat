@echo off

docker image prune --filter="label=unused" --force
docker container prune --all --force
docker volume prune --all --force
docker network prune --all --force
docker system prune --all --force
docker builder prune --all --force
docker buildx prune --all --force