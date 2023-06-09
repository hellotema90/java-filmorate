CREATE TABLE IF NOT EXISTS GENRE
(
    ID   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME VARCHAR
);

CREATE TABLE IF NOT EXISTS FILM
(
    ID          INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME        VARCHAR,
    DESCRIPTION VARCHAR(200),
    RELEASEDATE DATE,
    DURATION    INTEGER,
    RATE        VARCHAR
    );

CREATE TABLE IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INTEGER NOT NULL REFERENCES FILM (ID) ON DELETE CASCADE,
    GENRE_ID INTEGER NOT NULL REFERENCES GENRE (ID)
    );

CREATE TABLE IF NOT EXISTS MPA
(
    ID   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS FILM_MPA
(
    FILM_ID INTEGER NOT NULL REFERENCES FILM (ID)ON DELETE CASCADE,
    MPA_ID  INTEGER NOT NULL REFERENCES MPA (ID)
    );

CREATE TABLE IF NOT EXISTS USERS
(
    ID       INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    EMAIL    VARCHAR,
    LOGIN    VARCHAR NOT NULL,
    NAME     VARCHAR,
    BIRTHDAY DATE
);

CREATE TABLE IF NOT EXISTS LIKES
(
    FILM_ID INTEGER NOT NULL REFERENCES FILM (ID) ON DELETE CASCADE,
    USER_ID INTEGER NOT NULL REFERENCES USERS (ID) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS FRIENDSHIP
(
    USER_ID           INT REFERENCES USERS (ID) ON DELETE CASCADE,
    FRIEND_ID         INT REFERENCES USERS (ID) ON DELETE CASCADE
    );

INSERT INTO MPA (NAME)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

INSERT INTO GENRE (NAME)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');