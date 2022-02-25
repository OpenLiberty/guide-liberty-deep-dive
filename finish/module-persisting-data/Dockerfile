FROM openliberty/open-liberty:full-java11-openj9-ubi

ARG VERSION=1.0
ARG REVISION=SNAPSHOT

FROM postgres:14.1

COPY --chown=1001:0 src/main/liberty/config/server.xml /config/
COPY --chown=1001:0 target/liberty-deepdive-inventory.war /config/apps

# set env variable for password to docker
ENV POSTGRES_PASSWORD adminpwd
# create database called database
ENV POSTGRES_USER admin
# Use dumpfile to create table schema and populate it with data
# COPY *.sql /docker-entrypoint-initdb.d/


RUN    /etc/init.d/postgresql start &&\
    psql --command "CREATE USER docker WITH SUPERUSER PASSWORD 'docker';" &&\
    createdb -O docker docker