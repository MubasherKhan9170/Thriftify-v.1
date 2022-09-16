package com.example.thriftify.service.model.scan_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class Price {

    @SerializedName("thriftify_selling_price")
    @Expose
    private String thriftifySellingPrice;
    @SerializedName("lowest_available_price")
    @Expose
    private String lowestAvailablePrice;
    @SerializedName("source_postage_cost")
    @Expose
    private String sourcePostageCost;
    @SerializedName("thriftify_packaging_cost")
    @Expose
    private String thriftifyPackagingCost;
    @SerializedName("thriftify_postage_cost")
    @Expose
    private String thriftifyPostageCost;
    @SerializedName("thriftify_profitable_price")
    @Expose
    private String thriftifyProfitablePrice;
    @SerializedName("fee_charged_by_amazon")
    @Expose
    private String feeChargedByAmazon;
    @SerializedName("product_referral_fee_charged_by_amazon")
    @Expose
    private String productReferralFeeChargedByAmazon;
    @SerializedName("base_currency")
    @Expose
    private String baseCurrency;

    @NonNull
    public String getThriftifySellingPrice() {
        return thriftifySellingPrice;
    }

    public void setThriftifySellingPrice(@NonNull String thriftifySellingPrice) {
        this.thriftifySellingPrice = thriftifySellingPrice;
    }

    @NonNull
    public String getLowestAvailablePrice() {
        return lowestAvailablePrice;
    }

    public void setLowestAvailablePrice(@NonNull String lowestAvailablePrice) {
        this.lowestAvailablePrice = lowestAvailablePrice;
    }

    @NonNull
    public String getSourcePostageCost() {
        return sourcePostageCost;
    }

    public void setSourcePostageCost(@NonNull String sourcePostageCost) {
        this.sourcePostageCost = sourcePostageCost;
    }

    @NonNull
    public String getThriftifyPackagingCost() {
        return thriftifyPackagingCost;
    }

    public void setThriftifyPackagingCost(@NonNull String thriftifyPackagingCost) {
        this.thriftifyPackagingCost = thriftifyPackagingCost;
    }

    @NonNull
    public String getThriftifyPostageCost() {
        return thriftifyPostageCost;
    }

    public void setThriftifyPostageCost(@NonNull String thriftifyPostageCost) {
        this.thriftifyPostageCost = thriftifyPostageCost;
    }

    @NonNull
    public String getThriftifyProfitablePrice() {
        return thriftifyProfitablePrice;
    }

    public void setThriftifyProfitablePrice(@NonNull String thriftifyProfitablePrice) {
        this.thriftifyProfitablePrice = thriftifyProfitablePrice;
    }

    @NonNull
    public String getFeeChargedByAmazon() {
        return feeChargedByAmazon;
    }

    public void setFeeChargedByAmazon(@NonNull String feeChargedByAmazon) {
        this.feeChargedByAmazon = feeChargedByAmazon;
    }

    @NonNull
    public String getProductReferralFeeChargedByAmazon() {
        return productReferralFeeChargedByAmazon;
    }

    public void setProductReferralFeeChargedByAmazon(@NonNull String productReferralFeeChargedByAmazon) {
        this.productReferralFeeChargedByAmazon = productReferralFeeChargedByAmazon;
    }

    @NonNull
    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(@NonNull String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

}

