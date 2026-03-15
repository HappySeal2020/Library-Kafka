/*
  Create tables for Postgres
  BOOK
*/
DROP TABLE if exists author_to_book;
DROP TABLE if exists book_author_ids;
DROP TABLE if exists book;
DROP TABLE if exists author_cache;
DROP TABLE if exists publisher_cache;





CREATE TABLE author_cache (
                                    id INTEGER PRIMARY KEY NOT NULL UNIQUE,
                                    name VARCHAR(45) NOT NULL UNIQUE);

CREATE TABLE publisher_cache (
                                       id INTEGER PRIMARY KEY NOT NULL UNIQUE,
                                       name VARCHAR(45) NOT NULL UNIQUE,
                                       site VARCHAR(45) NULL);


CREATE TABLE book (
                                  id SERIAL PRIMARY KEY NOT NULL,
                                  name VARCHAR(150) NOT NULL UNIQUE,
                                  author_id INT NULL,
                                  print_year INT NULL,
                                  publisher_id INT NULL REFERENCES publisher_cache(id),
                                  bbk VARCHAR(45) NULL,
                                  isbn VARCHAR(45) NULL,
                                  pages INT NULL);

CREATE TABLE author_to_book (
                                            id SERIAL PRIMARY KEY NOT NULL,
                                            author_id INT NULL REFERENCES author_cache(id),
                                            book_id INT NULL REFERENCES book(id));
