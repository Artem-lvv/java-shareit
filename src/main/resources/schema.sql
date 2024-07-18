-- Создание таблицы пользователей
CREATE TABLE IF NOT EXISTS users
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(255)        NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

-- Создание таблицы запросов на предметы
CREATE TABLE IF NOT EXISTS item_requests
(
    id           SERIAL PRIMARY KEY,
    description  TEXT,
    requestor_id INT       NOT NULL,
    created      TIMESTAMP NOT NULL,
    FOREIGN KEY (requestor_id) REFERENCES users (id)
);

-- Создание таблицы предметов
CREATE TABLE IF NOT EXISTS items
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    owner_id    INT          NOT NULL,
    request_id  INT,
    available   BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (owner_id) REFERENCES users (id),
    FOREIGN KEY (request_id) REFERENCES item_requests (id)
);

-- Создание таблицы бронирований
CREATE TABLE IF NOT EXISTS bookings
(
    id        SERIAL PRIMARY KEY,
    start     TIMESTAMP   NOT NULL,
    "end"     TIMESTAMP   NOT NULL,
    item_id   INT         NOT NULL,
    booker_id INT         NOT NULL,
    status    VARCHAR(50) NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items (id),
    FOREIGN KEY (booker_id) REFERENCES users (id)
);

-- Создание таблицы комментариев
CREATE TABLE IF NOT EXISTS comments
(
    id        SERIAL PRIMARY KEY,
    text      TEXT      NOT NULL,
    item_id   INT       NOT NULL,
    author_id INT       NOT NULL,
    created   TIMESTAMP NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items (id),
    FOREIGN KEY (author_id) REFERENCES users (id)
);