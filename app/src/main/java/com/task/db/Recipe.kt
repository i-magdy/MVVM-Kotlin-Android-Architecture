package com.task.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "recipe_table")
data class Recipe(
    @PrimaryKey val id: String,
    val name: String,
    val headline: String,
    val description: String,
    val image: String,
    val thump: String,
    val isFavorite: Boolean = false
)
