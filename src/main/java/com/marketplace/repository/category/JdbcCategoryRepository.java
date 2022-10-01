package com.marketplace.repository.category;

import com.marketplace.service.category.ProductQuery;
import com.marketplace.service.category.CategoryShop;
import com.marketplace.service.category.SortingOption;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


public class JdbcCategoryRepository implements  CategoryRepository{

    private final JdbcTemplate template;
    private final ColumnConverter converter;

    public JdbcCategoryRepository(JdbcTemplate template, ColumnConverter converter) {
        this.template = template;
        this.converter = converter;
    }

    @Override
    public boolean categoryExists(long categoryId) {
        String sql = "SELECT EXISTS(" +
                "SELECT 1 " +
                "FROM categories " +
                "WHERE " +
                "category_id = ? AND " +
                "removed = FALSE " +
                "LIMIT 1)";

        return template.queryForObject(sql, Boolean.class, categoryId);
    }

    @Override
    public boolean isParentCategory(long categoryId) {
        String sql = "SELECT parent_category " +
                "FROM categories " +
                "WHERE category_id = ?";

        return template.queryForObject(
                sql,
                Boolean.class,
                categoryId
        );
    }

    @Override
    public boolean containsSubcategories(long categoryId) {
        String sql = "SELECT EXISTS(" +
                "SELECT 1 " +
                "FROM categories " +
                "WHERE " +
                "parent_id = ? AND " +
                "removed = FALSE " +
                "LIMIT 1)";

        return template.queryForObject(
                sql,
                Boolean.class,
                categoryId
        );
    }

    @Override
    public List<Category> getCategories(long parentId) {
        String sql = "SELECT " +
                "parent_id, " +
                "category_id, " +
                "name, " +
                "img_location, " +
                "parent_category " +
                "FROM categories " +
                "WHERE " +
                "parent_id = ? AND " +
                "removed = FALSE";

        return template.query(
                sql,
                new BeanPropertyRowMapper<Category>(Category.class),
                parentId
        );
    }

    @Override
    public List<Long> getProductsId(ProductQuery query) {
        String sql = "WITH id_list AS" +
                    "(SELECT " +
                        "products.product_id AS product_id, " +
                        "BOOL_AND(shop_products.removed) AS shop_products_removed, " +
                        "BOOL_AND(shops.removed) AS shops_removed, " +
                        "BOOL_AND(characteristic_id IN (" +
                            getInsertTemplate(query.getNumberOfCharacteristics()) +
                        ")) AS accepted_characteristics " +
                    "FROM products " +
                    "INNER JOIN shop_products " +
                        "USING(product_id) " +
                    "INNER JOIN shops " +
                        "USING(shop_id) " +
                    "INNER JOIN product_characteristics " +
                        "USING(product_id) " +
                    "WHERE " +
                        "products.removed = FALSE AND " +
                        "category_id = ? " +
                    "GROUP BY product_id " +
                    getOrderByStatement(query) + ") " +
                "SELECT product_id " +
                "FROM id_list " +
                "WHERE " +
                    "shop_products_removed = FALSE AND " +
                    "shops_removed = FALSE AND " +
                    "accepted_characteristics = TRUE";

        return template.queryForList(sql, Long.class, getQueryParameters(query));
    }

    private String getInsertTemplate(int numberOfParameters) {
        return String.join(
                ",",
                Collections.nCopies(numberOfParameters == 0 ? 1 : numberOfParameters, "?")
        );
    }

    private Object[] getQueryParameters(ProductQuery query) {
        List<Long> params = query.getCharacteristicsId();
        params.add(query.getCategoryId());

        return  params.toArray();
    }

    @Override
    public List<Long> getAllProductsId(ProductQuery query) {
        String sql = "WITH id_list AS" +
                    "(SELECT " +
                        "products.product_id AS product_id, " +
                        "BOOL_AND(shop_products.removed) AS shop_products_removed, " +
                        "BOOL_AND(shops.removed) AS shops_removed " +
                    "FROM products " +
                    "INNER JOIN shop_products " +
                        "USING(product_id) " +
                    "INNER JOIN shops " +
                        "USING(shop_id) " +
                    "INNER JOIN product_characteristics " +
                        "USING(product_id) " +
                    "WHERE " +
                        "products.removed = FALSE AND " +
                        "category_id = ? " +
                    "GROUP BY product_id " +
                    getOrderByStatement(query) + ") " +
                "SELECT product_id " +
                "FROM id_list " +
                "WHERE " +
                    "shop_products_removed = FALSE AND " +
                    "shops_removed = FALSE";

        return template.queryForList(sql, Long.class, query.getCategoryId());
    }

    private String getOrderByStatement(ProductQuery productQuery) {
        if(productQuery.getSortingOption() == SortingOption.NO_SORTING) {
            return "";
        } else {
            return "ORDER BY " +
                    converter.getColumnName(productQuery.getSortingOption()) +
                    (productQuery.isOrderDescending() ? " DESC " : " ASC ");
        }
    }

    @Override
    public List<ProductInfo> getProducts(List<Long> productsId) {
        String sql = "SELECT " +
                    "products.product_id AS product_id, " +
                    "products.name AS name, " +
                    "products.img_location AS img_location, " +
                    "MIN(price) AS min_price, " +
                    "MAX(price) AS max_price, " +
                    "SUM(reviews) AS total_reviews " +
                "FROM products " +
                "INNER JOIN shop_products " +
                    "USING(product_id) " +
                "WHERE " +
                    "shop_products.removed = FALSE AND " +
                    "product_id IN(" +
                        getInsertTemplate(productsId.size()) +
                    ") " +
                "GROUP BY product_id ";

        Object[] params = productsId.toArray();

        return template.query(
                sql,
                new BeanPropertyRowMapper<ProductInfo>(ProductInfo.class),
                params.length == 0 ? new Object[] {-1} : params
        );
    }

    @Override
    public long addCategory(Category category) {
        String sql = "INSERT INTO categories(" +
                "name, " +
                "parent_id, " +
                "img_location, " +
                "parent_category) " +
                "VALUES (?, ?, ?, ?) " +
                "RETURNING category_id";

        try {
            return template.queryForObject(
                    sql,
                    Long.class,
                    category.getName(),
                    category.getParentId(),
                    category.getImgLocation(),
                    category.isParentCategory()
            );
        } catch (Exception ex) {
            return -1;
        }
    }

    @Override
    public boolean addShopToCategory(long shopId, long categoryId) {
        String sql = "INSERT INTO category_shops(shop_id, category_id) " +
                "VALUES(?,?)";

        return 1 == template.update(sql, shopId, categoryId);
    }

    @Override
    public long addShop(CategoryShop shop) {
        String sql = "INSERT INTO shops(" +
                "name, " +
                "img_location, " +
                ") " +
                "VALUES(?,?) " +
                "RETURNING shop_id";

        try {
            return template.queryForObject(
                    sql,
                    Long.class,
                    shop.getName(),
                    shop.getImgLocation()
            );
        } catch (Exception ex) {
            return -1;
        }
    }

    @Transactional
    @Override
    public boolean removeShop(long shopId) {
        int updated = 0;

        String removeShop = "UPDATE shops " +
                "SET removed = TRUE " +
                "WHERE shop_id = ?";
        updated += template.update(removeShop, shopId);

        String removeShopProducts = "UPDATE shop_products " +
                "SET removed = TRUE " +
                "WHERE shop_id = ?";
        updated += template.update(removeShopProducts, shopId);

        return updated == getCountOfShopUpdatedEntities(shopId);
    }

    private int getCountOfShopUpdatedEntities(long shopId) {
        String sql = "SELECT COUNT(*) " +
                "FROM shop_products " +
                "WHERE shop_id = ?";

        return 1 + template.queryForObject(sql, Integer.class, shopId);
    }

    @Override
    public List<CategoryShop> getShops(long categoryId) {
        String sql = "SELECT " +
                "bool_or(category_id = ?) AS present_in_category, " +
                "shop_id, " +
                "name, " +
                "img_location " +
                "FROM shops " +
                "INNER JOIN category_shops " +
                "USING(shop_id) " +
                "WHERE removed = FALSE " +
                "GROUP BY shop_id ";

        return template.query(sql, new BeanPropertyRowMapper<CategoryShop>(CategoryShop.class), categoryId);
    }

    @Override
    public long addCharacteristic(Characteristic characteristic) {
        String sql = "INSERT INTO characteristics(" +
                "category_id, " +
                "name, " +
                "characteristic_value) " +
                "VALUES (?, ?, ?) " +
                "RETURNING characteristic_id";

        try {
            return template.queryForObject(
                    sql,
                    Long.class,
                    characteristic.getCategoryId(),
                    characteristic.getName(),
                    characteristic.getCharacteristicValue()
            );
        } catch (Exception ex) {
            return -1;
        }
    }

    @Override
    public List<Characteristic> getCharacteristics(long categoryId) {
        String sql = "SELECT " +
                    "characteristic_id, " +
                    "category_id, " +
                    "name, " +
                    "characteristic_value " +
                "FROM characteristics " +
                "WHERE category_id = ?";

        return template.query(sql,
                new BeanPropertyRowMapper<Characteristic>(Characteristic.class),
                categoryId);
    }

    @Override
    public List<Long> getCharacteristicsId(long categoryId) {
        String sql = "SELECT characteristic_id " +
                "FROM product_characteristics " +
                "INNER JOIN products " +
                "USING(product_id) " +
                "WHERE category_id = ?";

        return template.queryForList(sql, Long.class, categoryId);
    }

    @Override
    @Transactional
    public boolean removeCategory(long categoryId) {
        int updated = 0;

        String removeCategory = "UPDATE categories " +
                "SET removed = TRUE " +
                "WHERE category_id = ?";
        updated += template.update(removeCategory, categoryId);

        String removeProducts = "UPDATE products " +
                "SET removed = TRUE " +
                "WHERE category_id = ?";
        updated += template.update(removeProducts, categoryId);

        String removeShopProducts = "UPDATE shop_products " +
                "SET removed = TRUE " +
                "FROM products " +
                "WHERE products.removed = TRUE";
        updated += template.update(removeShopProducts);

        return updated == getCountOfCategoryUpdatedEntities(categoryId);
    }

    private long getCountOfCategoryUpdatedEntities(long categoryId) {
        int count = 0;

        String  countProducts = "SELECT COUNT(*) " +
                "FROM products " +
                "WHERE category_id = ? ";
        count += template.queryForObject(countProducts, Integer.class, categoryId);

        String countShopProducts = "WITH updated_products AS (" +
                "SELECT product_id " +
                "FROM products " +
                "WHERE category_id = ?) " +
                "SELECT COUNT(*) " +
                "FROM shop_products " +
                "INNER JOIN updated_products " +
                "USING(product_id)";
        count += template.queryForObject(countShopProducts, Integer.class, categoryId);

        return 1 + count;
    }
}
