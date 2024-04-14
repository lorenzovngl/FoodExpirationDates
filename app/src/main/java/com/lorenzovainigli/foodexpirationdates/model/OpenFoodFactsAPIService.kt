package com.lorenzovainigli.foodexpirationdates.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsAPIService {

    @GET("{barcode}?fields=brands,product_name,code,image_thumb_url")
    suspend fun getProduct(@Path("barcode") barcode: String): Response<OpenFoodFactsJsonResponse>

}