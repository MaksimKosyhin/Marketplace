CREATE TABLE categories
  (
     category_id     INT GENERATED always AS IDENTITY,
     name            VARCHAR(50),
     parent_id       INT,
     img_location    VARCHAR(255) NOT NULL,
     removed         BOOLEAN DEFAULT FALSE,
     PRIMARY KEY(category_id),
     CONSTRAINT fk_parent_category FOREIGN KEY(parent_id) REFERENCES categories(
     category_id)
  );

CREATE TABLE products
  (
     product_id      INT GENERATED always AS IDENTITY,
     name            VARCHAR(90) NOT NULL,
     category_id     INT NOT NULL,
     img_location    VARCHAR(255) NOT NULL,
     removed         BOOLEAN DEFAULT FALSE,
     PRIMARY KEY(product_id),
     CONSTRAINT fk_category FOREIGN KEY(category_id) REFERENCES categories(category_id)
     ON DELETE CASCADE
  );

CREATE TABLE characteristics
  (
     characteristic_id    INT GENERATED always AS IDENTITY,
     name                 VARCHAR(90) NOT NULL,
     characteristic_value VARCHAR(90) NOT NULL,
     category_id          INT NOT NULL,
     PRIMARY KEY(characteristic_id),
     CONSTRAINT fk_category FOREIGN KEY(category_id) REFERENCES categories(category_id)
     ON DELETE CASCADE
  );

CREATE TABLE product_characteristics
  (
     product_id        INT,
     characteristic_id INT,
     PRIMARY KEY(product_id, characteristic_id),
     CONSTRAINT fk_product FOREIGN KEY(product_id) REFERENCES products(product_id) ON DELETE CASCADE,
     CONSTRAINT fk_characteristic FOREIGN KEY(characteristic_id) REFERENCES
     characteristics(characteristic_id) ON DELETE CASCADE
  );

CREATE TABLE shops
  (
     shop_id      INT GENERATED always AS IDENTITY,
     name         VARCHAR(90) NOT NULL,
     link         VARCHAR(90) NOT NULL,
     img_location VARCHAR(255) NOT NULL,
     removed      BOOLEAN DEFAULT FALSE,
     PRIMARY KEY(shop_id)
  );

CREATE TABLE category_shops
  (
     category_id INT,
     shop_id     INT,
     PRIMARY KEY(category_id, shop_id),
     CONSTRAINT fk_category FOREIGN KEY(category_id) REFERENCES categories(category_id) ON DELETE CASCADE,
     CONSTRAINT fk_shop FOREIGN KEY(shop_id) REFERENCES shops(shop_id) ON DELETE CASCADE
  );

CREATE TABLE shop_products
  (
     shop_id    INT,
     product_id INT,
     score      INT NOT NULL,
     price      INT NOT NULL,
     reviews    INT NOT NULL,
     removed    BOOLEAN DEFAULT FALSE,
     PRIMARY KEY(shop_id, product_id),
     CONSTRAINT fk_shop FOREIGN KEY(shop_id) REFERENCES shops(shop_id) ON DELETE CASCADE,
     CONSTRAINT fk_product FOREIGN KEY(product_id) REFERENCES products(product_id) ON DELETE CASCADE
  );

CREATE TYPE user_role AS ENUM('ADMIN', 'MODERATOR');

CREATE TABLE users
  (
     user_id  INT GENERATED always AS IDENTITY,
     username VARCHAR(90) UNIQUE NOT NULL,
     password TEXT NOT NULL,
     role user_role,
     registration_date DATE NOT NULL DEFAULT CURRENT_DATE,
     PRIMARY KEY(user_id)
  );

CREATE TABLE orders
  (
     order_id          INT GENERATED always AS IDENTITY,
     user_id           INT NOT NULL,
     registration_date DATE,
     PRIMARY KEY(order_id),
     CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
  );

CREATE TABLE order_shop_products
  (
     order_id   INT,
     product_id INT,
     shop_id    INT,
     amount     INT NOT NULL CHECK (amount > 0),
     PRIMARY KEY(order_id, product_id, shop_id),
     CONSTRAINT fk_order FOREIGN KEY(order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
     CONSTRAINT fk_product FOREIGN KEY(product_id) REFERENCES products(product_id) ON DELETE CASCADE,
     CONSTRAINT fk_shop FOREIGN KEY(shop_id) REFERENCES shops(shop_id) ON DELETE CASCADE
  );