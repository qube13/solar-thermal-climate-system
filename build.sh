#!/bin/bash

# build images
docker build -t smueller18/base:python3-alpine ./base/python3-alpine
docker build -t smueller18/python3-kafka:alpine ./base/python3-kafka/alpine
docker build -t smueller18/consumer:monitor ./consumer/monitor
docker build -t smueller18/consumer:postgres ./consumer/postgres
