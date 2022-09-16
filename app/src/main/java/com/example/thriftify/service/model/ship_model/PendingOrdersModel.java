package com.example.thriftify.service.model.ship_model;

public class PendingOrdersModel {
    private String product_name = "Pending" ;

    public PendingOrdersModel(final String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_name() {
        return this.product_name;
    }

    public void setProduct_name(final String product_name) {
        this.product_name = product_name;
    }
}
