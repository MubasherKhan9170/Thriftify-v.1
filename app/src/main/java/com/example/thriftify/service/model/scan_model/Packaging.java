package com.example.thriftify.service.model.scan_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class Packaging {

    @SerializedName("estimated_letter_type")
    @Expose
    private String estimatedLetterType;

    @NonNull
    public String getEstimatedLetterType() {
        return estimatedLetterType;
    }

    public void setEstimatedLetterType(@NonNull String estimatedLetterType) {
        this.estimatedLetterType = estimatedLetterType;
    }

}
