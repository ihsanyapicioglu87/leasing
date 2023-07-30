CREATE DATABASE IF NOT EXISTS leasing;
USE leasing;

CREATE TABLE IF NOT EXISTS brand (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS model (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       brand_id INT NOT NULL,
                       FOREIGN KEY (brand_id) REFERENCES brand(id)
);
