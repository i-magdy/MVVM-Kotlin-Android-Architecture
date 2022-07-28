package com.task.ui.component.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.task.R
import com.task.RECIPE_ITEM_KEY
import com.task.databinding.DetailsLayoutBinding
import com.task.db.Recipe
import com.task.ui.base.BaseActivity
import com.task.utils.observe
import com.task.utils.toGone
import com.task.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by AhmedEltaher
 */

@AndroidEntryPoint
class DetailsActivity : BaseActivity() {

    private val viewModel: DetailsViewModel by viewModels()

    private lateinit var binding: DetailsLayoutBinding
    private var menu: Menu? = null


    override fun initViewBinding() {
        binding = DetailsLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.pbLoading.toVisible()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        this.menu = menu
        viewModel.initIntentData(intent.getStringExtra(RECIPE_ITEM_KEY) ?: "")
        return true
    }

    fun onClickFavorite(mi: MenuItem) {
        viewModel.onFavouriteClicked()
    }

    override fun observeViewModel() {
        observe(viewModel.recipeData, ::initializeView)
    }


    private fun handleIsFavouriteUI(isFavourite: Boolean) {
        menu?.let {
            it.findItem(R.id.add_to_favorite)?.icon =
                ContextCompat.getDrawable(this,if (isFavourite) R.drawable.ic_star_24 else R.drawable.ic_outline_star_border_24)
        }
    }

    private fun initializeView(recipesItem: Recipe) {
        binding.tvName.text = recipesItem.name
        binding.tvHeadline.text = recipesItem.headline
        binding.tvDescription.text = recipesItem.description
        Picasso.get().load(recipesItem.image).placeholder(R.drawable.ic_healthy_food_small)
            .into(binding.ivRecipeImage)
        binding.pbLoading.toGone()
        handleIsFavouriteUI(recipesItem.isFavorite)
    }
}
