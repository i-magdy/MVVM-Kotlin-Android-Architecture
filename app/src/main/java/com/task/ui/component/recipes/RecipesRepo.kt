package com.task.ui.component.recipes

import com.task.data.DataRepositorySource
import com.task.data.Resource
import com.task.data.dto.recipes.RecipesItem
import com.task.db.AppDao
import com.task.db.Recipe
import com.task.utils.wrapEspressoIdlingResource
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipesRepo @Inject constructor(
    private val dataRepositoryRepository: DataRepositorySource,
    private val db: AppDao
){
    val recipesStateFlow = MutableStateFlow<Resource<List<Recipe>>>(Resource.Loading())
    private val coroutineJob = CoroutineScope(Dispatchers.IO)

    init { sync() }

    fun sync(){
        coroutineJob.launch {
            launch {
                db.getRecipes().collect {
                    recipesStateFlow.value = Resource.Success(it)
                }
            }
        }
    }

    fun getRecipes(){
        coroutineJob.launch {
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestRecipes().collect {
                   it.data?.recipesList?.forEach { item -> insertRecipeToDb(recipesItem = item) }
                }
            }
        }
    }

    fun onSearchInRecipes(text: String){
        coroutineJob.launch {
            db.searchRecipes(text = text).collect {
                recipesStateFlow.value = Resource.Success(it)
            }
        }
    }


    private fun insertRecipeToDb(recipesItem: RecipesItem){
        db.insertRecipe(
            recipes = Recipe(
                id = recipesItem.id,
                name = recipesItem.name,
                description = recipesItem.description,
                headline = recipesItem.headline,
                image = recipesItem.image,
                thump = recipesItem.thumb
            )
        ).subscribeOn(Schedulers.computation())
            .subscribe()
    }

    fun cancelJob(){
        coroutineJob.cancel()
    }
}