package com.task.ui.component.recipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.task.databinding.RecipeItemBinding
import com.task.db.Recipe
import com.task.ui.base.listeners.RecyclerItemListener
import com.task.ui.component.recipes.RecipesListViewModel

/**
 * Created by AhmedEltaher
 */

class RecipesAdapter(private val recipesListViewModel: RecipesListViewModel, private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeViewHolder>() {

    private val onItemClickListener: RecyclerItemListener = object : RecyclerItemListener {
        override fun onItemSelected(recipeId: String) {
            recipesListViewModel.openRecipeDetails(recipeId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemBinding = RecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}


