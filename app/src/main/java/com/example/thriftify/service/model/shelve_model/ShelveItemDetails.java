package com.example.thriftify.service.model.shelve_model;

public class ShelveItemDetails {
    private String product_name = "ABC";
    private String product_sku = "23444-78923497sdf89b";
    private String product_location = "112";
    private String product_price = "9.08";
    private String product_listed_since = "Friday, 22 January 2021";
    private String product_edition = "";
    private String product_type = "Paperback";
    private String product_authors = "James clear";
    private String product_status = "Active";

    public ShelveItemDetails(final String product_name, final String product_sku, final String product_location, final String product_price, final String product_listed_since, final String product_edition, final String product_type, final String product_authors, final String product_status) {
        this.product_name = product_name;
        this.product_sku = product_sku;
        this.product_location = product_location;
        this.product_price = product_price;
        this.product_listed_since = product_listed_since;
        this.product_edition = product_edition;
        this.product_type = product_type;
        this.product_authors = product_authors;
        this.product_status = product_status;
    }

    public String getProduct_name() {
        return this.product_name;
    }

    public void setProduct_name(final String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_sku() {
        return this.product_sku;
    }

    public void setProduct_sku(final String product_sku) {
        this.product_sku = product_sku;
    }

    public String getProduct_location() {
        return this.product_location;
    }

    public void setProduct_location(final String product_location) {
        this.product_location = product_location;
    }

    public String getProduct_price() {
        return this.product_price;
    }

    public void setProduct_price(final String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_listed_since() {
        return this.product_listed_since;
    }

    public void setProduct_listed_since(final String product_listed_since) {
        this.product_listed_since = product_listed_since;
    }

    public String getProduct_edition() {
        return this.product_edition;
    }

    public void setProduct_edition(final String product_edition) {
        this.product_edition = product_edition;
    }

    public String getProduct_type() {
        return this.product_type;
    }

    public void setProduct_type(final String product_type) {
        this.product_type = product_type;
    }

    public String getProduct_authors() {
        return this.product_authors;
    }

    public void setProduct_authors(final String product_authors) {
        this.product_authors = product_authors;
    }

    public String getProduct_status() {
        return this.product_status;
    }

    public void setProduct_status(final String product_status) {
        this.product_status = product_status;
    }
}
