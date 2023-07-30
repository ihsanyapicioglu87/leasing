CREATE TABLE IF NOT EXISTS vehicle (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         brand_id INT NOT NULL,
                         model_id INT NOT NULL,
                         model_year INT NOT NULL,
                         vin VARCHAR(255),
                         price DECIMAL(10, 2) NOT NULL,
                         FOREIGN KEY (brand_id) REFERENCES brand(id),
                         FOREIGN KEY (model_id) REFERENCES model(id)
);

CREATE TABLE IF NOT EXISTS users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS role (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_role (
                           user_id INT NOT NULL,
                           role_id INT NOT NULL,
                           PRIMARY KEY (user_id, role_id),
                           FOREIGN KEY (user_id) REFERENCES users(id),
                           FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE IF NOT EXISTS customer (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL,
                          birthdate DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS leasing_contract (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  contract_no VARCHAR(255) NOT NULL UNIQUE,
                                  monthly_rate DECIMAL(19, 2) NOT NULL,
                                  customer_id INT,
                                  vehicle_id INT,
                                  FOREIGN KEY (customer_id) REFERENCES customer (id),
                                  FOREIGN KEY (vehicle_id) REFERENCES vehicle (id)
);

CREATE TABLE IF NOT EXISTS audit_log (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           action VARCHAR(255),
                           entity_type VARCHAR(255),
                           entity_id INT,
                           username VARCHAR(255),
                           timestamp DATETIME
);
