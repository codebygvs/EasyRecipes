package com.devspace.myapplication



data class RecipesDTO (
    val id: Int,
    val title: String,
    val image: String,
    val summary: String,
    val glutenFree: String,
    val vegetarian: String,
    val vegan: String,
    val veryHealthy: String,
    val veryPopular: String

) {
}

data class RecipeResponse(

    val results: List<RecipesDTO>
)
