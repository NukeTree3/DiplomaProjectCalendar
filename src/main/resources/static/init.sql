CREATE TABLE date_note
(
    id        SERIAL PRIMARY KEY,
    date      DATE,
    author    VARCHAR(255),
    text_note TEXT
)

CREATE TABLE messages
(
    id           SERIAL PRIMARY KEY,
    from_user_id VARCHAR(255),
    to_user_id   VARCHAR(255),
    message_text TEXT,
    time         VARCHAR(255)
)

CREATE TABLE user_friends
(
    id            SERIAL PRIMARY KEY,
    user_friends  VARCHAR(255)[],
    friend_offers VARCHAR(255)[],
    send_offer    VARCHAR(255)[]
);

CREATE TABLE user_roles
(
    user_id         INT,
    user_role       VARCHAR(255),
    activation_code VARCHAR(255)
);

CREATE TABLE users
(
    id        SERIAL PRIMARY KEY,
    username  VARCHAR(255),
    password  VARCHAR(255),
    firstname VARCHAR(255),
    lastname  VARCHAR(255),
    email     VARCHAR(255),
    birthdate DATE
)

CREATE TABLE users_photo
(
    user_id  SERIAL PRIMARY KEY,
    photourl VARCHAR(255)
)

CREATE
OR REPLACE FUNCTION update_user_role()
RETURNS TRIGGER AS $$
BEGIN
  IF
NEW.user_role = 'ROLE_USER' AND OLD.user_role = 'ROLE_NOT_ACTIVATED' THEN
    INSERT INTO users_photo (user_id)
    VALUES (NEW.user_id);

INSERT INTO user_friends (id)
VALUES (NEW.user_id);

END IF;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER user_role_update
    AFTER UPDATE
    ON user_roles
    FOR EACH ROW
    EXECUTE FUNCTION update_user_role();