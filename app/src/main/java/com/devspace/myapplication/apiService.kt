package com.devspace.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface apiService {
    @GET("recipes/random?number=10")
    fun getRandom(): Call<RecipeResponse>

    @GET("recipes/{id}/informationBulk?includeNutrition=false")
    fun getRecipeInformation(@Path("id") id: String): Call<RecipesDTO>
}