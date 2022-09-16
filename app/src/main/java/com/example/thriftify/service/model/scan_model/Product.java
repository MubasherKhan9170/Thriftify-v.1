package com.example.thriftify.service.model.scan_model;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.thriftify.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class Product {

    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_type")
    @Expose
    private String productType;
    @SerializedName("product_image_url")
    @Expose
    private String productImageUrl;
    @SerializedName("product_barcode")
    @Expose
    private String productBarcode;
    @SerializedName("product_barcode_type")
    @Expose
    private String productBarcodeType;
    @SerializedName("product_genre")
    @Expose
    private String productGenre;
    @SerializedName("product_language")
    @Expose
    private String productLanguage;
    @SerializedName("product_author")
    @Expose
    private String productAuthor;
    @SerializedName("product_publisher")
    @Expose
    private String productPublisher;
    @SerializedName("product_edition")
    @Expose
    private String productEdition;
    @SerializedName("product_label")
    @Expose
    private String productLabel;
    @SerializedName("product_hardware")
    @Expose
    private String productHardware;
    @SerializedName("product_platform")
    @Expose
    private String productPlatform;
    @SerializedName("number_of_discs")
    @Expose
    private String numberOfDiscs;
    @SerializedName("disc_type")
    @Expose
    private String discType;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("disc_format")
    @Expose
    private String discFormat;
    @SerializedName("dimensions")
    @Expose
    private Dimensions dimensions;
    @SerializedName("product_categories")
    @Expose
    private List<String> productCategories;
    @SerializedName("price")
    @Expose
    private Price price;
    @SerializedName("exchange_rate")
    @Expose
    private ExchangeRate exchangeRate;
    @SerializedName("packaging")
    @Expose
    private Packaging packaging;

    @NonNull
    public String getProductName() {
        return productName;
    }

    public void setProductName(@NonNull String productName) {
        this.productName = productName;
    }

    @NonNull
    public String getProductType() {
        return productType;
    }

    public void setProductType(@NonNull String productType) {
        this.productType = productType;
    }

    @NonNull
    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(@NonNull String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    @NonNull
    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(@NonNull String productBarcode) {
        this.productBarcode = productBarcode;
    }

    @NonNull
    public String getProductBarcodeType() {
        return productBarcodeType;
    }

    public void setProductBarcodeType(@NonNull String productBarcodeType) {
        this.productBarcodeType = productBarcodeType;
    }

    @NonNull
    public String getProductGenre() {
        return productGenre;
    }

    public void setProductGenre(@NonNull String productGenre) {
        this.productGenre = productGenre;
    }

    @NonNull
    public String getProductLanguage() {
        return productLanguage;
    }

    public void setProductLanguage(@NonNull String productLanguage) {
        this.productLanguage = productLanguage;
    }

    @NonNull
    public String getProductAuthor() {
        return productAuthor;
    }

    public void setProductAuthor(@NonNull String productAuthor) {
        this.productAuthor = productAuthor;
    }

    @NonNull
    public String getProductPublisher() {
        return productPublisher;
    }

    public void setProductPublisher(@NonNull String productPublisher) {
        this.productPublisher = productPublisher;
    }

    @NonNull
    public String getProductEdition() {
        return productEdition;
    }

    public void setProductEdition(@NonNull String productEdition) {
        this.productEdition = productEdition;
    }

    @NonNull
    public String getProductLabel() {
        return productLabel;
    }

    public void setProductLabel(@NonNull String productLabel) {
        this.productLabel = productLabel;
    }

    @NonNull
    public String getProductHardware() {
        return productHardware;
    }

    public void setProductHardware(@NonNull String productHardware) {
        this.productHardware = productHardware;
    }

    @NonNull
    public String getProductPlatform() {
        return productPlatform;
    }

    public void setProductPlatform(@NonNull String productPlatform) {
        this.productPlatform = productPlatform;
    }

    @NonNull
    public String getNumberOfDiscs() {
        return numberOfDiscs;
    }

    public void setNumberOfDiscs(@NonNull String numberOfDiscs) {
        this.numberOfDiscs = numberOfDiscs;
    }

    @NonNull
    public String getDiscType() {
        return discType;
    }

    public void setDiscType(@NonNull String discType) {
        this.discType = discType;
    }

    @NonNull
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(@NonNull String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @NonNull
    public String getDiscFormat() {
        return discFormat;
    }

    public void setDiscFormat(@NonNull String discFormat) {
        this.discFormat = discFormat;
    }

    @NonNull
    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(@NonNull Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    @NonNull
    public List<String> getProductCategories() {
        return Collections.unmodifiableList(this.productCategories);
    }

    public void setProductCategories(@NonNull List<String> productCategories) {
        this.productCategories = Collections.unmodifiableList(productCategories);
    }

    @NonNull
    public Price getPrice() {
        return price;
    }

    public void setPrice(@NonNull Price price) {
        this.price = price;
    }

    @NonNull
    public ExchangeRate getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(@NonNull ExchangeRate exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @NonNull
    public Packaging getPackaging() {
        return packaging;
    }

    public void setPackaging(@NonNull Packaging packaging) {
        this.packaging = packaging;
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(@NonNull final ImageView view, @NonNull final String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl).apply(new RequestOptions().centerCrop()
                .error(R.mipmap.ic_splash_screen_icon_round))
                .into(view);
    }
    @BindingAdapter("cardVisible")
    public static void changeVisibilityCard(@NonNull View view, Object visible) {
        view.setVisibility(visible==null ? View.VISIBLE: View.GONE);
    }

    @BindingAdapter("LayoutVisible")
    public static void changeVisibilityLayout(@NonNull View view, boolean visible) {
        view.setVisibility(visible==true? View.VISIBLE: View.GONE);
    }

}
