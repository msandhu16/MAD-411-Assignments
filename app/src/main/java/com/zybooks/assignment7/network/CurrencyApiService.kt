package com.zybooks.assignment7.network

import android.icu.util.Currency
import com.zybooks.assignment7.Cost
import retrofit2.Retrofit
import retrofit2.http.GET

interface CurrencyApiService {
    @GET("currencies.json")
    suspend fun getCurrencyCode(): Map<String, String>


    @GET("currencies/cad.json")
    suspend fun getPrice(): Cost

}