package com.example.githubusers.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubusers.database.FavoriteUser
import com.example.githubusers.repository.FavoriteRepository

class FavoriteViewModel(application: Application): ViewModel() {

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFav(): LiveData<List<FavoriteUser>> {
        return mFavoriteRepository.getAllFavorite()
    }
}