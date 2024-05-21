package com.example.githubusers.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubusers.database.FavoriteDao
import com.example.githubusers.database.FavoriteDatabase
import com.example.githubusers.database.FavoriteUser
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getDatabase(application)
        mFavoriteDao = db.FavoriteDao()
    }

    fun getAllFavorite(): LiveData<List<FavoriteUser>> = mFavoriteDao.getAllFavorite()

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return mFavoriteDao.getFavoriteUserByUsername(username)
    }

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteDao.delete(favoriteUser) }
    }
}