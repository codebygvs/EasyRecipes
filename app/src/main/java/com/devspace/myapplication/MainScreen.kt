package com.devspace.myapplication

import android.util.Log
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.devspace.myapplication.ui.theme.EasyRecipesTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MainScreen(navHostController: NavHostController) {

    var recipes by rememberSaveable { mutableStateOf<List<RecipesDTO>>(emptyList()) }
    val retrofit = RetrofitClient.retrofitInstance.create(apiService::class.java)

    if (recipes.isEmpty()) {
        retrofit.getRandom().enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(
                call: Call<RecipeResponse>,
                response: Response<RecipeResponse>
            ) {
                if (response.isSuccessful) {
                    recipes = response.body()?.results ?: emptyList()

                } else {
                    Log.d("MainActivity", "Request Error ::${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.d("MainActivity", "Network Error ::${t.message}")
            }

        })
    }

    RecipesListContent(
        recipes = recipes,
        onClick = { itemClicked ->
            navHostController.navigate(route = "recipeDetail/${itemClicked.id}")
        }
    )
}

@Composable
private fun RecipesListContent(
    recipes: List<RecipesDTO>,
    onClick: (RecipesDTO) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {

        RecipesSession(
            label = "Recipes",
            recipesList = recipes,
            onClick = onClick
        )
    }
}

@Composable
private fun RecipesSession(
    label: String,
    recipesList: List<RecipesDTO>,
    onClick: (RecipesDTO) -> Unit
) {
    Text(
        fontSize = 24.sp,
        text = label,
        fontWeight = FontWeight.SemiBold
    )
    RecipeList(
        recipesList = recipesList,
        onClick = onClick
    )
}


@Composable
private fun RecipeList(
    recipesList: List<RecipesDTO>,
    onClick: (RecipesDTO) -> Unit
) {

    LazyRow(
        modifier = Modifier
            .padding(16.dp)
    ) {
        items(recipesList) { recipe: RecipesDTO ->
            RecipesItem(
                recipeDTO = recipe,
                onClick = onClick
            )
        }
    }

}

@Composable
fun RecipesItem(
    recipeDTO: RecipesDTO,
    onClick: (RecipesDTO) -> Unit
) {

    Column(
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .clickable {
                onClick.invoke(recipeDTO)
            }
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(end = 4.dp)
                .width(120.dp)
                .height(150.dp),
            contentScale = ContentScale.Crop,
            model = recipeDTO.image,
            contentDescription = "${recipeDTO.title} image"
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            fontWeight = FontWeight.SemiBold,
            maxLines = 3,
            text = recipeDTO.title
        )
        Text(
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            text = recipeDTO.summary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipesListPreview() {
    EasyRecipesTheme {
        RecipesListContent(
            recipes = emptyList(),
            onClick = {}
        )

    }

}

