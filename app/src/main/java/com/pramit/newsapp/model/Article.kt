package com.pramit.newsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.SharedFlow
import java.util.UUID

@Entity(tableName = "articles")
data class Article(@PrimaryKey val id: String = UUID.randomUUID().toString(),
                   val title: String = "",
                   val description: String? = null,
                   val urlToImage: String? = null,
                   val content: String? = null)
