package com.example.githubusers.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusers.data.response.DetailUserResponse
import com.example.githubusers.data.retrofit.ApiConfig
import com.example.githubusers.database.FavoriteUser
import com.example.githubusers.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application): ViewModel() {

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun getDetailUser(username: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


    fun addFavorite(username: String?, avatarUrl: String?) {
        val favUser = if (avatarUrl != null) {
            FavoriteUser(username!!, avatarUrl)
        } else {
            val detailUser = _detailUser.value
            if (detailUser != null) {
                FavoriteUser(username!!, detailUser.avatarUrl)
            } else {
                FavoriteUser(username!!, null)
            }
        }
        mFavoriteRepository.insert(favUser)
    }

    fun checkFavorite(username: String?): LiveData<FavoriteUser> {
        return mFavoriteRepository.getFavoriteUserByUsername(username!!)
    }

    fun deleteFavorite(favoriteUser: FavoriteUser) {
        mFavoriteRepository.delete(favoriteUser)
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}