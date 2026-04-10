package com.lorenzovainigli.foodexpirationdates.model

import com.google.gson.annotations.SerializedName

data class OpenFoodFactsJsonResponse(
    @SerializedName("code")
    var code: String,
    @SerializedName("product")
    var product: Product,
    @SerializedName("status")
    var status: Int,
    @SerializedName("status_verbose") val statusVerbose: String?
)

data class Product(
    @SerializedName("brands")
    var brands: String?,
    @SerializedName("product_name")
    var productName: String?,
    @SerializedName("image_thumb_url")
    var imageThumbUrl: String?
)