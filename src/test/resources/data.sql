INSERT INTO brand (name) VALUES ('Brand1');
INSERT INTO brand (name) VALUES ('Brand2');
INSERT INTO brand (name) VALUES ('Brand3');

INSERT INTO model (name, brand_id) VALUES ('Model1-Brand1', 1);
INSERT INTO model (name, brand_id) VALUES ('Model2-Brand1', 1);
INSERT INTO model (name, brand_id) VALUES ('Model1-Brand2', 2);
INSERT INTO model (name, brand_id) VALUES ('Model2-Brand2', 2);
INSERT INTO model (name, brand_id) VALUES ('Model1-Brand3', 3);
INSERT INTO model (name, brand_id) VALUES ('Model2-Brand3', 3);

INSERT INTO customer (first_name, last_name, birthdate) VALUES ('John', 'Doe', '1990-01-01');
INSERT INTO customer (first_name, last_name, birthdate) VALUES ('Jane', 'Smith', '1985-05-15');
INSERT INTO customer (first_name, last_name, birthdate) VALUES ('Michael', 'Johnson', '1978-10-20');

INSERT INTO vehicle (brand_id, model_id, model_year, vin, price) VALUES (1, 1, 2022, 'VIN111', 25000.00);
INSERT INTO vehicle (brand_id, model_id, model_year, vin, price) VALUES (2, 3, 2021, 'VIN222', 28000.00);
INSERT INTO vehicle (brand_id, model_id, model_year, vin, price) VALUES (3, 5, 2023, 'VIN333', 30000.00);

INSERT INTO leasing_contract (contract_no, monthly_rate, customer_id, vehicle_id) VALUES ('LC1001', 500.00, 1, 1);
INSERT INTO leasing_contract (contract_no, monthly_rate, customer_id, vehicle_id) VALUES ('LC1002', 550.00, 2, 2);
INSERT INTO leasing_contract (contract_no, monthly_rate, customer_id, vehicle_id) VALUES ('LC1003', 600.00, 3, 3);
