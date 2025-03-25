ALTER TABLE users
ADD CONSTRAINT users_login_unique UNIQUE (login);