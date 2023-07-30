INSERT INTO brand (name)
VALUES ('BMW'),
       ('Mercedes'),
       ('Audi');

INSERT INTO model (name, brand_id)
VALUES ('X1', 1),
       ('X6', 1),
       ('C200', 2),
       ('A4', 3);

INSERT INTO vehicle (brand_id, model_id, model_year, vin, price)
VALUES (1, 1, 2022, 'VIN123323453', 25000.00),
       (1, 2, 2021, 'VIN956757455', 32000.00),
       (2, 3, 2020, 'VIN456767656', 23000.00),
       (3, 4, 2023, 'VIN783453455', 30000.00);

INSERT INTO users (username, password)
VALUES ('user', '$2a$10$L7kwET1F3Pr4AkakrDdmM.n22Y8IROztWuvbX0Uw.K5JbmYTVEgFi'),
       ('admin', '$2a$10$SROfKz1ymxfq7AuhQ7MJO.NZ19P5HRL0oK1GadSQZEpwp0vgwChG.');

INSERT INTO role (name)
VALUES ('ADMIN'),
       ('USER');
