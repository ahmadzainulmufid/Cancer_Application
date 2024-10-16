package com.dicoding.asclepius

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.information.ArticlesItem
import com.dicoding.asclepius.data.information.InformationCancer
import com.dicoding.asclepius.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InformationViewModel : ViewModel() {

    private val _information = MutableLiveData<List<ArticlesItem>>()
    val information: LiveData<List<ArticlesItem>> = _information

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getInformation()
    }

    private fun getInformation() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val client = ApiConfig.getApiService().getHealthNews()
            client.enqueue(object : Callback<InformationCancer> {
                override fun onResponse(
                    call: Call<InformationCancer>,
                    response: Response<InformationCancer>
                ) {
                    _isLoading.postValue(false)
                    if (response.isSuccessful) {
                        val articles = response.body()?.articles ?: emptyList()
                        _information.postValue(articles.filter { article ->
                            val title = article.title ?: ""
                            val description = article.description?.toString() ?: ""
                            val urlToImage = article.urlToImage?.toString() ?: ""

                            title.isNotEmpty() &&
                                    description.isNotEmpty() &&
                                    urlToImage.isNotEmpty()
                        })
                    } else {
                        _information.postValue(emptyList())
                    }
                }

                override fun onFailure(call: Call<InformationCancer>, t: Throwable) {
                    _isLoading.postValue(false)
                }
            })
        }
    }
}