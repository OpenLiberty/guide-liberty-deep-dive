FROM postgres:17.5

# create database called database
ENV POSTGRES_USER=admin

# Use dumpfile to create table schema and populate it with data
COPY schema.sql /docker-entrypoint-initdb.d/1-schema.sql

EXPOSE 5432
