package com.pramit.newsapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pramit.newsapp.dao.NewsDao
import com.pramit.newsapp.model.Article

@Database(entities = [Article::class], version = 5, exportSchema = false)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun newsDao(): NewsDao
}