CREATE DATABASE IF NOT EXISTS food_delivery_db;
USE food_delivery_db;

CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    contact_no BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS restaurants (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS food_items (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    restaurant_id INT NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS delivery_persons (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact_no BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_items (
    customer_id INT NOT NULL,
    food_id INT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (customer_id, food_id),
    FOREIGN KEY (customer_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES food_items(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'Pending',
    delivery_person_id INT NULL,
    delivery_address VARCHAR(255) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES users(user_id),
    FOREIGN KEY (delivery_person_id) REFERENCES delivery_persons(id)
);

CREATE TABLE IF NOT EXISTS order_items (
    order_id INT NOT NULL,
    food_id INT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (order_id, food_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES food_items(id)
);

-- Optional sample data
INSERT IGNORE INTO restaurants(id, name) VALUES (101, 'HariOmDhaba'), (102, 'ExpressInn');
INSERT IGNORE INTO food_items(id, name, price, restaurant_id)
VALUES (1, 'PanjabiThali', 340, 101), (2, 'PavBhaji', 140, 101);
