CREATE TABLE categories (
id INT GENERATED ALWAYS AS IDENTITY,
name VARCHAR(50),
parent_id INT NOT NULL,
PRIMARY KEY(id),
CONSTRAINT fk_parent_category 
	FOREIGN KEY(parent_id) 
		REFERENCES categories(id)
);

CREATE TABLE products (
id INT GENERATED ALWAYS AS IDENTITY,
name VARCHAR(90) NOT NULL,
photo BYTEA NOT NULL,
category_id INT NOT NULL,
PRIMARY KEY(id),
CONSTRAINT fk_category 
	FOREIGN KEY(category_id)
		REFERENCES categories(id)
);

CREATE TABLE characteristics (
id INT GENERATED ALWAYS AS IDENTITY,
name VARCHAR(90) NOT NULL,
value VARCHAR(90) NOT NULL,
category_id INT NOT NULL,
PRIMARY KEY(id),
CONSTRAINT fk_category 
	FOREIGN KEY(category_id)
		REFERENCES categories(id)
);

CREATE TABLE product_characteristics (
product_id INT,
characteristic_id INT,
PRIMARY KEY(product_id, characteristic_id),
CONSTRAINT fk_product
	FOREIGN KEY(product_id)
		REFERENCES products(id),
CONSTRAINT fk_characteristic
	FOREIGN KEY(characteristic_id)
		REFERENCES characteristics(id)
);

CREATE TABLE shops(
id INT GENERATED ALWAYS AS IDENTITY,
name VARCHAR(90) NOT NULL,
link VARCHAR(90) NOT NULL,
shop_icon BYTEA NOT NULL,
PRIMARY KEY(id)
);

CREATE TABLE shop_products(
shop_id INT,
product_id INT,
score INT NOT NULL,
price INT NOT NULL,
reviews INT NOT NULL,
PRIMARY KEY(shop_id, product_id),
CONSTRAINT fk_shop
	FOREIGN KEY(shop_id)
		REFERENCES shops(id),
CONSTRAINT fk_product
	FOREIGN KEY(product_id)
		REFERENCES products(id)
);

CREATE TABLE users(
id INT GENERATED ALWAYS AS IDENTITY,
username VARCHAR(90) NOT NULL,
password TEXT NOT NULL,
PRIMARY KEY(id)
);

CREATE TABLE orders(
id INT GENERATED ALWAYS AS IDENTITY,
user_id INT NOT NULL,
PRIMARY KEY(id),
CONSTRAINT fk_user
	FOREIGN KEY(user_id)
		REFERENCES users(id)
);

CREATE TABLE order_shop_products(
order_id INT,
product_id INT,
shop_id INT,
amount INT CHECK (amount > 0),
PRIMARY KEY(order_id, product_id, shop_id),
CONSTRAINT fk_order
	FOREIGN KEY(order_id)
		REFERENCES orders(id),
CONSTRAINT fk_product
	FOREIGN KEY(product_id)
		REFERENCES products(id),
CONSTRAINT fk_shop
	FOREIGN KEY(shop_id)
		REFERENCES shops(id)
);