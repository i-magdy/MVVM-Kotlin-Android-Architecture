package com.task.ui.component.recipes.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.task.R
import com.task.data.dto.recipes.RecipesItem
import com.task.databinding.RecipeItemBinding
import com.task.db.Recipe
import com.task.ui.base.listeners.RecyclerItemListener

/**
 * Created by AhmedEltaher
 */

class RecipeViewHolder(private val itemBinding: RecipeItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(recipesItem: Recipe, recyclerItemListener: RecyclerItemListener) {
        itemBinding.tvCaption.text = recipesItem.description
        itemBinding.tvName.text = recipesItem.name
        Picasso.get().load(recipesItem.thump).placeholder(R.drawable.ic_healthy_food).error(R.drawable.ic_healthy_food).into(itemBinding.ivRecipeItemImage)
        itemBinding.rlRecipeItem.setOnClickListener { recyclerItemListener.onItemSelected(recipesItem.id) }
        itemBinding.itemFavouriteLabel.visibility = if(recipesItem.isFavorite) View.VISIBLE else View.GONE
    }
}

