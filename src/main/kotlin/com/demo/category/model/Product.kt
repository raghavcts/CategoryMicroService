package com.demo.category.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class Product(val productId: String, val title: String, val nowPrice: String, val priceLabel: String,
                   val colorSwatches: List<ColorSwatches>) {

    @JsonIgnore
    var maxReduction: Float? = 0.0f
}