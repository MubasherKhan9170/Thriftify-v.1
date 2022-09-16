package com.example.thriftify.service.model.scan_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class ExchangeRate {

    @SerializedName("base_currency")
    @Expose
    private String baseCurrency;
    @SerializedName("to_currency")
    @Expose
    private String toCurrency;
    @SerializedName("exchange_date")
    @Expose
    private String exchangeDate;
    @SerializedName("exchange_rate")
    @Expose
    private String exchangeRate;
    @SerializedName("currency_pair")
    @Expose
    private String currencyPair;

    @NonNull
    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(@NonNull String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    @NonNull
    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(@NonNull String toCurrency) {
        this.toCurrency = toCurrency;
    }

    @NonNull
    public String getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(@NonNull String exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    @NonNull
    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(@NonNull String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @NonNull
    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(@NonNull String currencyPair) {
        this.currencyPair = currencyPair;
    }

}


