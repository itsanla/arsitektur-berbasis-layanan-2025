FROM jenkins/jenkins:lts-jdk17

USER root

# Basic setup
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

USER jenkins