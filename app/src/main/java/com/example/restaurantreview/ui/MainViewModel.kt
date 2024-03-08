package com.example.restaurantreview.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantreview.data.response.CustomerReviewsItem
import com.example.restaurantreview.data.response.PostReviewResponse
import com.example.restaurantreview.data.response.Restaurant
import com.example.restaurantreview.data.response.RestaurantResponse
import com.example.restaurantreview.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.restaurantreview.utils.Event

class MainViewModel : ViewModel() {

    //variable baru dengan event materi baru
    private val _snackbartext = MutableLiveData<Event<String>>()
    val  snackbartext : LiveData<Event<String>> = _snackbartext
    private val _restaurant = MutableLiveData<Restaurant>()
    val restaurant : LiveData<Restaurant> = _restaurant

    private val _listReview = MutableLiveData<List<CustomerReviewsItem>>()
    val listReview : LiveData<List<CustomerReviewsItem>> = _listReview

    private val  _isloading = MutableLiveData<Boolean>()
    val isloading : LiveData<Boolean> = _isloading

    //variable baru untuk menyimpan text pada materi baru yaitu live data single event
//    private val  _snackbartext = MutableLiveData<String>()
//    val snackbartext : LiveData<String> = _snackbartext

    companion object{
        private const val  TAG = "MainViewModel"
        private const val  RESTAURANT_ID = "uewq1zg2zlskfw1e867"
    }

    init {
        findRestaurant()
    }
    private fun findRestaurant() {
        _isloading.value = true
        val client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID)
        client.enqueue(object : Callback<RestaurantResponse> {
            override fun onResponse(
                call: Call<RestaurantResponse>,
                response: Response<RestaurantResponse>
            ) {
                _isloading.value = true
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _restaurant.value = response.body()?.restaurant
                        _listReview.value = response.body()?.restaurant?.customerReviews
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RestaurantResponse>, t: Throwable) {
                _isloading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun postReview(review: String) {
        _isloading.value = true
        val client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Dicoding", review)
        client.enqueue(object : Callback<PostReviewResponse> {
            override fun onResponse(
                call: Call<PostReviewResponse>,
                response: Response<PostReviewResponse>
            ) {
                _isloading.value = true
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _listReview.value = response.body()?.customerReviews
                    _snackbartext.value = Event(response.body()?.message.toString())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                _isloading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}