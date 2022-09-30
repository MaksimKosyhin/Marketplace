
INSERT INTO categories(name, img_location, parent_category) VALUES ('electronics', 'path1', TRUE);
INSERT INTO categories(parent_id, name, img_location, parent_category) VALUES (2, 'laptops', 'path2', FALSE);
INSERT INTO categories(parent_id, name, img_location, parent_category) VALUES (2, 'tablets', 'path3', FALSE);
INSERT INTO categories(parent_id, name, img_location, parent_category, removed) VALUES (2, 'phones', 'path4', FALSE, TRUE);

INSERT INTO products(name, category_id, img_location) VALUES ('laptopA', 3, 'path5');
INSERT INTO products(name, category_id, img_location) VALUES ('laptopB', 3, 'path6');
INSERT INTO products(name, category_id, img_location, removed) VALUES ('laptopC', 3, 'path7', TRUE);

INSERT INTO shops(name, img_location) VALUES ('shopA', 'path8');
INSERT INTO shops(name, img_location) VALUES ('shopB', 'path9');
INSERT INTO shops(name, img_location) VALUES ('shopC', 'path10');
INSERT INTO shops(name, img_location, removed) VALUES ('shopD', 'path11', TRUE);

INSERT INTO category_shops(category_id, shop_id) VALUES(3, 1);
INSERT INTO category_shops(category_id, shop_id) VALUES(3, 2);
INSERT INTO category_shops(category_id, shop_id) VALUES(4, 1);
INSERT INTO category_shops(category_id, shop_id) VALUES(4, 3);

INSERT INTO shop_products(product_id, shop_id, link, score, price, reviews)
    VALUES(1, 1, 'link1', 3, 4, 2);
INSERT INTO shop_products(product_id, shop_id, link, score, price, reviews)
    VALUES(2, 1, 'link2', 4, 3, 10);
INSERT INTO shop_products(product_id, shop_id, link, score, price, reviews, removed)
    VALUES(3, 1, 'link3', 5, 6, 5, TRUE);

INSERT INTO shop_products(product_id, shop_id, link, score, price, reviews)
    VALUES(1, 2, 'link4', 3, 5, 50);
INSERT INTO shop_products(product_id, shop_id, link, score, price, reviews)
    VALUES(2, 2, 'link5', 3, 4, 5);
INSERT INTO shop_products(product_id, shop_id, link, score, price, reviews)
    VALUES(3, 2, 'link6', 3, 4, 5);

INSERT INTO shop_products(product_id, shop_id, link, score, price, reviews)
    VALUES(1, 3, 'link7', 4, 10, 15);
INSERT INTO shop_products(product_id, shop_id, link, score, price, reviews)
    VALUES(2, 3, 'link8', 8, 2, 20);
INSERT INTO shop_products(product_id, shop_id, link, score, price, reviews)
    VALUES(3, 3, 'link9', 1, 2, 18);

INSERT INTO shop_products(product_id, shop_id, link, score, price, reviews, removed)
    VALUES(1, 4, 'link10', 7, 22, 10, TRUE);

INSERT INTO characteristics(category_id, name, characteristic_value) VALUES (3, 'color', 'red');
INSERT INTO characteristics(category_id, name, characteristic_value) VALUES (3, 'color', 'black');

INSERT INTO product_characteristics(product_id, characteristic_id) VALUES (1, 1);
INSERT INTO product_characteristics(product_id, characteristic_id) VALUES (2, 1);
INSERT INTO product_characteristics(product_id, characteristic_id) VALUES (3, 2);

INSERT INTO users(username, password) VALUES ('John', '123');
INSERT INTO users(username, password) VALUES ('Marta', '456');

INSERT INTO orders(user_id, registration_date) VALUES(1, '2020-10-23');
INSERT INTO orders(user_id, registration_date) VALUES(1, '2020-11-13');

INSERT INTO orders(user_id, registration_date) VALUES(2, '2021-04-30');

INSERT INTO order_shop_products(order_id, shop_id, product_id, amount) VALUES (1, 1, 1, 12);
INSERT INTO order_shop_products(order_id, shop_id, product_id, amount) VALUES (1, 1, 2, 3);
INSERT INTO order_shop_products(order_id, shop_id, product_id, amount) VALUES (1, 2, 3, 4);

INSERT INTO order_shop_products(order_id, shop_id, product_id, amount) VALUES (2, 2, 1, 6);
INSERT INTO order_shop_products(order_id, shop_id, product_id, amount) VALUES (2, 3, 2, 5);

INSERT INTO order_shop_products(order_id, shop_id, product_id, amount) VALUES (3, 1, 1, 10);