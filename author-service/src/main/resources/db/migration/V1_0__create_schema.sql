/*
  Create tables for Postgres
  AUTHOR
*/
DROP TABLE if exists author;
                                      

CREATE TABLE author (
                                    id SERIAL PRIMARY KEY NOT NULL,
                                    name VARCHAR(45) NOT NULL UNIQUE);
