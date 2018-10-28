FROM ubuntu:latest
MAINTAINER Nicola Nardino, https://github.com/NicolaNardino

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH ${PATH}:$JAVA_HOME/bin/

RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y ant && \
    apt-get clean;
    
RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;