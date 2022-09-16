package com.example.thriftify.service.model.scan_model;

import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

public class ProductDetail {
    @Inject
    public ProductDetail() {
    }

    @SerializedName("is_salable")
    @Expose
    private boolean isSalable = false;
    @SerializedName("product")
    @Expose
    private Product product = null;
    @SerializedName("error")
    @Expose
    private Object error = null;

    public boolean isIsSalable() {
        return isSalable;
    }

    public void setIsSalable(boolean isSalable) {
        this.isSalable = isSalable;
    }

    @Nullable
    public Product getProduct() {
        return product;
    }

    public void setProduct(@Nullable Product product) {
        this.product = product;
    }

    @Nullable
    public Object getError() {
        return error;
    }

    public void setError(@Nullable Object error) {
        this.error = error;
    }

    @BindingAdapter({"salable", "error"})
    public static void changeVisibilitySalable(@NonNull View view, boolean visible, Object error) {
        view.setVisibility(visible!=true?error!=null?View.VISIBLE: View.GONE:View.GONE);
    }
    @BindingAdapter("visibleIfError")
    public static void changeVisibilityError(@NonNull View view, Object visible) {
        view.setVisibility(visible!=null ? View.VISIBLE: View.GONE);
    }

}
