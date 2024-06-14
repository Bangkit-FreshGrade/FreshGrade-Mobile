package com.example.freshgrade.ui.main.article

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freshgrade.R
import com.example.freshgrade.adapter.ArticleAdapter
import com.example.freshgrade.data.api.ApiConfig
import com.example.freshgrade.data.response.ArticleResponse
import com.example.freshgrade.databinding.FragmentArticleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ArticleViewModel by viewModels()

    private lateinit var articleAdapter: ArticleAdapter
    private var articleList: List<ArticleResponse> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.articleRv.layoutManager = LinearLayoutManager(requireContext())
        articleAdapter = ArticleAdapter(articleList)
        binding.articleRv.adapter = articleAdapter

        fetchArticles()
    }

    private fun fetchArticles() {
        val call = ApiConfig.getApiService().getArticles()
        call.enqueue(object : Callback<List<ArticleResponse>> {
            override fun onResponse(call: Call<List<ArticleResponse>>, response: Response<List<ArticleResponse>>) {
                if (response.isSuccessful) {
                    response.body()?.let { articles ->
                        articleAdapter.updateData(articles)
                    }
                } else {
                    Log.e("ArticleFragment", "Request failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<ArticleResponse>>, t: Throwable) {
                Log.e("ArticleFragment", "Request failed: ${t.message}")
            }
        })
    }
}