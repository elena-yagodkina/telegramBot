-- liquibase formatted sql

-- changeset elenaya:1

CREATE TABLE notification_task (
    id serial PRIMARY KEY,
    chat_id int8 NOT NULL,
    message varchar(250),
    date_time timestamp
)