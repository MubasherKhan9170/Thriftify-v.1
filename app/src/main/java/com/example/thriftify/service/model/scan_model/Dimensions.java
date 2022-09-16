package com.example.thriftify.service.model.scan_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class Dimensions {

    @SerializedName("package_weight")
    @Expose
    private String packageWeight;
    @SerializedName("package_width")
    @Expose
    private String packageWidth;
    @SerializedName("package_height")
    @Expose
    private String packageHeight;
    @SerializedName("package_length")
    @Expose
    private String packageLength;
    @SerializedName("measuring_unit")
    @Expose
    private String measuringUnit;

    @NonNull
    public String getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(@NonNull String packageWeight) {
        this.packageWeight = packageWeight;
    }

    @NonNull
    public String getPackageWidth() {
        return packageWidth;
    }

    public void setPackageWidth(@NonNull String packageWidth) {
        this.packageWidth = packageWidth;
    }

    @NonNull
    public String getPackageHeight() {
        return packageHeight;
    }

    public void setPackageHeight(@NonNull String packageHeight) {
        this.packageHeight = packageHeight;
    }

    @NonNull
    public String getPackageLength() {
        return packageLength;
    }

    public void setPackageLength(@NonNull String packageLength) {
        this.packageLength = packageLength;
    }

    @NonNull
    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(@NonNull String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

}

