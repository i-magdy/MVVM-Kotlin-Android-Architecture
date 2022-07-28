package com.task.ui.component.recipes

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.task.data.DataRepositorySource
import com.task.data.Resource
import com.task.data.dto.recipes.Recipes
import com.task.data.dto.recipes.RecipesItem
import com.task.db.Recipe
import com.task.ui.base.BaseViewModel
import com.task.utils.SingleEvent
import com.task.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale.ROOT
import javax.inject.Inject

/**
 * Created by AhmedEltaher
 */
@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val repo: RecipesRepo
) : BaseViewModel() {

    /**
     * Data --> LiveData, Exposed as LiveData, Locally in viewModel as MutableLiveData
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val recipesLiveDataPrivate = MutableLiveData<Resource<List<Recipe>>>()
    val recipesLiveData: LiveData<Resource<List<Recipe>>> get() = recipesLiveDataPrivate


    //TODO check to make them as one Resource
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val recipeSearchFoundPrivate: MutableLiveData<Recipe> = MutableLiveData()
    val recipeSearchFound: LiveData<Recipe> get() = recipeSearchFoundPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val noSearchFoundPrivate: MutableLiveData<Unit> = MutableLiveData()
    val noSearchFound: LiveData<Unit> get() = noSearchFoundPrivate

    /**
     * UI actions as event, user action is single one time event, Shouldn't be multiple time consumption
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val openRecipeDetailsPrivate = MutableLiveData<SingleEvent<String>>()
    val openRecipeDetails: LiveData<SingleEvent<String>> get() = openRecipeDetailsPrivate

    init {
        viewModelScope.launch {
            repo.recipesStateFlow.collect {
                recipesLiveDataPrivate.value = it
            }
        }
    }
    /**
     * Error handling as UI
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate


    fun getRecipes() {
        viewModelScope.launch {
            repo.getRecipes()
        }
    }

    fun openRecipeDetails(recipeId: String) {
        openRecipeDetailsPrivate.value = SingleEvent(recipeId)
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }

    fun onSearchClick(recipeName: String) {
        repo.onSearchInRecipes(text = recipeName)
    }

    fun onSearchClosed() = repo.sync()


    override fun onCleared() {
        super.onCleared()
        repo.cancelJob()
    }
}
