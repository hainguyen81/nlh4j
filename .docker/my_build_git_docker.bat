@echo off

SET /p GIT_TOKEN=<git.token
echo [ ------- GIT_TOKEN: %GIT_TOKEN% ------- ]

build_git_docker.bat hainguyen81 %GIT_TOKEN% master