package com.example.freshgrade.ui.main.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshgrade.data.response.ArticleResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ArticleViewModel : ViewModel() {

    private val _articles = MutableLiveData<List<ArticleResponse>>()
    val articles: LiveData<List<ArticleResponse>> get() = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchArticles() {
        _isLoading.value = true
        viewModelScope.launch {
            val fetchedArticles = fetchArticlesFromNetwork()
            _articles.value = fetchedArticles
            _isLoading.value = false
        }
    }

    private suspend fun fetchArticlesFromNetwork(): List<ArticleResponse> {
        delay(2000)
        return listOf(
        )
    }
}
