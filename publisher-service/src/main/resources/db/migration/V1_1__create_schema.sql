/*
  Create tables for Postgres
   PUBLISHER
*/
DROP TABLE if exists publisher;

CREATE TABLE publisher (
                                       id SERIAL PRIMARY KEY NOT NULL,
                                       name VARCHAR(45) NOT NULL UNIQUE,
                                       site VARCHAR(45) NULL);
