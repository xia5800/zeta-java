#!/bin/bash
docker-compose down
docker rmi zeta_java_server
docker-compose up -d
