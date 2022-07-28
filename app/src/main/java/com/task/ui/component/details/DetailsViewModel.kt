package com.task.ui.component.details

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.task.data.Resource
import com.task.db.AppDao
import com.task.db.Recipe
import com.task.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by AhmedEltaher
 */
@HiltViewModel
open class DetailsViewModel @Inject constructor(
    private val db: AppDao
) : BaseViewModel() {

    private var _recipeId: String = ""
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val recipePrivate = MutableLiveData<Recipe>()
    val recipeData: LiveData<Recipe> get() = recipePrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val isFavouritePrivate = MutableLiveData<Resource<Boolean>>()
    val isFavourite: LiveData<Resource<Boolean>> get() = isFavouritePrivate

    fun initIntentData(recipeId: String) {
        if (recipeId.isEmpty()) return
        _recipeId = recipeId
        viewModelScope.launch {
            db.getRecipeById(recipeId = _recipeId).collect {
                recipePrivate.value = it
            }
        }
    }


    fun onFavouriteClicked() {
        viewModelScope.launch {
            val  completable = if (recipePrivate.value?.isFavorite!!){
                db.removeRecipeFromFavourites(_recipeId)
            }else{
                db.addRecipeToFavourite(_recipeId)
            }
            completable.subscribeOn(Schedulers.computation())
                .subscribe()
        }
    }
}
