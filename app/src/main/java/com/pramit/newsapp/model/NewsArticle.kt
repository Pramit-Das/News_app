package com.pramit.newsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_articles")
data class NewsArticle(@PrimaryKey val id: String,
                       val title: String,
                       val description: String?,
                       val imageUrl: String?,
                       val content: String?)
