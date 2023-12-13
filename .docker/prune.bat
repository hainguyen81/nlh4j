@echo off

docker image prune --filter="label=unused" --force
docker container prune --force
docker volume prune --all --force
docker network prune --force
docker system prune --all --force
docker builder prune --all --force
docker buildx prune --all --force
docker buildx rm --all-inactive --force