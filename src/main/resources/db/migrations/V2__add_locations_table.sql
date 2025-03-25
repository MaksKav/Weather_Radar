CREATE TABLE Locations (
    id SERIAL PRIMARY KEY ,
    name VARCHAR NOT NULL ,
    user_id INTEGER NOT NULL ,
    latitude DECIMAL(9,6) NOT NULL ,
    longitude DECIMAL(9,6) NOT NULL ,

    CONSTRAINT fk_location_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);