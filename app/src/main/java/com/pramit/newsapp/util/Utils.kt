package com.pramit.newsapp.util

import com.pramit.newsapp.model.Article
import java.util.UUID

// this code will return unique id for news article to store in DB.
fun List<Article>.ensureUniqueIds(): List<Article> {
    return this.map { article ->
        val safeId = article.id.takeIf { !it.isNullOrEmpty() } ?: UUID.randomUUID().toString()
        article.copy(id = safeId)
    }
}