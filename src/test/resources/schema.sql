CREATE SCHEMA IF NOT EXISTS leasing;

CREATE TABLE IF NOT EXISTS leasing.brand (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS leasing.model (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     name VARCHAR(255) NOT NULL,
                                     brand_id BIGINT NOT NULL,
                                     FOREIGN KEY (brand_id) REFERENCES brand (id)
);

CREATE TABLE IF NOT EXISTS leasing.customer (
                                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        first_name VARCHAR(255) NOT NULL,
                                        last_name VARCHAR(255) NOT NULL,
                                        birthdate DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS leasing.vehicle (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       brand_id BIGINT NOT NULL,
                                       model_id BIGINT NOT NULL,
                                       model_year INT,
                                       vin VARCHAR(255),
                                       price DECIMAL(10, 2) NOT NULL,
                                       FOREIGN KEY (brand_id) REFERENCES brand (id),
                                       FOREIGN KEY (model_id) REFERENCES model (id)
);

CREATE TABLE IF NOT EXISTS leasing.leasing_contract (
                                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                contract_no VARCHAR(255) NOT NULL UNIQUE,
                                                monthly_rate DECIMAL(10, 2) NOT NULL,
                                                customer_id BIGINT NOT NULL,
                                                vehicle_id BIGINT NOT NULL,
                                                FOREIGN KEY (customer_id) REFERENCES customer (id),
                                                FOREIGN KEY (vehicle_id) REFERENCES vehicle (id)
);
