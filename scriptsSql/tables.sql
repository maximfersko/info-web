DROP TABLE IF EXISTS checks CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS peers CASCADE;
DROP TABLE IF EXISTS tasks CASCADE;

CREATE TABLE peers
(
    pk_nickname TEXT PRIMARY KEY,
    birthday    DATE
);

CREATE TABLE tasks
(
    pk_title    TEXT PRIMARY KEY,
    parent_task TEXT,
    max_xp      INTEGER,
    CONSTRAINT fk_tasks_parent_task
        FOREIGN KEY (parent_task)
            REFERENCES tasks (pk_title)
);

CREATE TABLE checks
(
    id   SERIAL PRIMARY KEY,
    peer TEXT,
    task TEXT,
    date TIMESTAMP,
    CONSTRAINT fk_checks_peer
        FOREIGN KEY (peer)
            REFERENCES peers (pk_nickname),
    CONSTRAINT fk_checks_task
        FOREIGN KEY (task)
            REFERENCES tasks (pk_title)
);

CREATE TABLE friends
(
    id    SERIAL PRIMARY KEY,
    peer1 TEXT,
    peer2 TEXT,
    CONSTRAINT fk_friends_peer1
        FOREIGN KEY (peer1)
            REFERENCES peers (pk_nickname),
    CONSTRAINT fk_friends_peer2
        FOREIGN KEY (peer2)
            REFERENCES peers (pk_nickname),
    CONSTRAINT uc_friends_unique_pairs
        UNIQUE (peer1, peer2)
);

INSERT INTO tasks(
    pk_title, parent_task, max_xp)
VALUES ('SimpleBashUtils', null, 250),
       ('Linux', 'SimpleBashUtils', 500),
       ('LinuxNetwork', 'SimpleBashUtils', 300),
       ('CPP_matrix', 'SimpleBashUtils', 350),
       ('CPP_containers', 'SimpleBashUtils', 750);

INSERT INTO peers(
    pk_nickname, birthday)
VALUES ('lolek', '1994-12-11'),
       ('bolek', '1995-04-03'),
       ('stolas', '666-06-06'),
       ('poison', '1992-01-15'),
       ('remedy', '2000-11-26');

INSERT INTO friends(
    peer1, peer2)
VALUES ('lolek', 'bolek'),
       ('poison', 'lolek'),
       ('poison', 'bolek'),
       ('remedy', 'lolek'),
       ('remedy', 'bolek');

INSERT INTO checks(
    peer, task, date)
VALUES ('bolek', 'SimpleBashUtils', '2022-10-19 10:23:54'),
       ('lolek', 'SimpleBashUtils', '2022-10-19 11:20:50'),
       ('poison', 'CPP_matrix', '2022-08-19 18:13:00'),
       ('remedy', 'CPP_matrix', '2022-09-19 21:43:45'),
       ('stolas', 'SimpleBashUtils', '2022-06-06 05:11:43'),
       ('stolas', 'Linux', '2022-06-06 06:45:25'),
       ('stolas', 'LinuxNetwork', '2022-06-06 07:20:11');