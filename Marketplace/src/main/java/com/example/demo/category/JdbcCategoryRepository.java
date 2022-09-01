package com.example.demo.category;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcCategoryRepository implements  CategoryRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcCategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean isParentCategory(long categoryId) {
        String sql = "SELECT (SELECT * FROM categories WHERE parent_id = ? LIMIT 1) NOT NULL";
        return jdbcTemplate.queryForObject(sql, Boolean.class, categoryId);
    }

    @Override
    public List<String> getCategories(long parentId) {
        String sql = "SELECT name FROM categories WHERE parent_id = ?";
        return jdbcTemplate.queryForList(sql, String.class, parentId);
    }

    @Override
    public List<ShopProduct> getProducts(long categoryId) {
        String sql = "SELECT products.product_id AS product_id, " +
                "name, " +
                "img_location, " +
                "MIN(price) AS min_price, " +
                "MAX(price) AS max_price " +
                "FROM products " +
                "WHERE category_id = ? AND removed = false" +
                "LEFT JOIN shop_products USING product_id " +
                "GROUP BY product_id";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<ShopProduct>());
    }

    @Override
    public void addCategory(String name, long parentId) {
        String sql = "INSERT INTO categories (name, parent_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, name, parentId);
    }

    @Override
    public void addCharacteristic(long categoryId, String name, String value) {
        String sql = "INSERT INTO characteristics (category_id, name, value) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, categoryId, name, value);
    }

    @Override
    public List<Characteristic> getCategoryCharacteristics(long categoryId) {
        String sql = "SELECT name, value FROM characteristics WHERE categoryId = ?";
        return  jdbcTemplate.query(sql, new BeanPropertyRowMapper<Characteristic>(), categoryId);
    }

    @Override
    public void removeCategory(long categoryId) {
        String removeCategory = "UPDATE categories SET removed = TRUE WHERE category_id = ?";
        jdbcTemplate.update(removeCategory, categoryId);

        String removeProducts = "UPDATE products SET removed = TRUE WHERE category_id = ?";
        jdbcTemplate.update(removeProducts, categoryId);

        String removeProductShops = "UPDATE shop_products SET removed = TRUE WHERE product_id IN " +
                "(SELECT product_id FROM products WHERE removed = TRUE)";
        jdbcTemplate.update(removeProductShops);
    }

    //TODO: move this functionality into another repository and service
//    public Path getProductPath(long productId) {
//        Path pathToProduct = imgDirectory;
//        CategoryProduct product = this.getProduct(productId);
//        Category category = this.getCategory(product.getCategoryId());
//
//        Stack<String> stack = new Stack<>();
//        stack.push(product.getName());
//
//        while(category.getParentId() != -1) {
//            stack.push(category.getName());
//            category = this.getCategory(category.getParentId());
//        }
//
//        while(!stack.empty()) {
//            pathToProduct.resolve(stack.pop());
//        }
//
//        return pathToProduct;
//    }
//
//    private CategoryProduct getProduct(long productId) {
//        String sql = "SELECT name, category_id FROM products WHERE productId = ?";
//        return jdbcTemplate
//                .query(sql, new BeanPropertyRowMapper<CategoryProduct>(), productId)
//                .get(0);
//    }
//
//    private Category getCategory(long categoryId) {
//        String sql = "SELECT name, parent_id FROM categories WHERE category_id = ?";
//        return  jdbcTemplate
//                .query(sql, new BeanPropertyRowMapper<Category>(), categoryId)
//                .get(0);
//    }
}
