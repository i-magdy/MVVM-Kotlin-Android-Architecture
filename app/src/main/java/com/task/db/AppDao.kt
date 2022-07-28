package com.task.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Query("SELECT * FROM recipe_table order by id asc")
    fun getRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipe_table where id = :recipeId")
    fun getRecipeById(recipeId: String): Flow<Recipe?>

    @Query("UPDATE recipe_table set isFavorite = 1 where id = :recipeId")
    fun addRecipeToFavourite(recipeId: String): Completable

    @Query("UPDATE recipe_table set isFavorite = 0 where id = :recipeId")
    fun removeRecipeFromFavourites(recipeId: String): Completable

    @Query("SELECT * from recipe_table where name LIKE '%' || :text || '%' order by name asc")
    fun searchRecipes(text: String): Flow<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertRecipe(recipes: Recipe): Completable
}