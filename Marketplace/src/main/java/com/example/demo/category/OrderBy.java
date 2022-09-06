package com.example.demo.category;

public enum OrderBy {
    SCORE_ASC("ORDER BY average_score ASC"),
    SCORE_DESC("ORDER BY average_score DESC"),
    REVIEWS_ASC("ORDER BY total_reviews ASC"),
    REVIEWS_DESC("ORDER BY total_reviews DESC"),
    PRICE_ASC("ORDER BY min_price ASC"),
    PRICE_DESC("ORDER BY max_price DESC");
    private final String sql;

    OrderBy(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return this.sql;
    }
 }
