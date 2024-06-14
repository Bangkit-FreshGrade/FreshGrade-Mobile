package com.example.freshgrade.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.freshgrade.R
import com.example.freshgrade.data.response.ArticleResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ArticleAdapter(private var articleList: List<ArticleResponse>) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.article_title)
        val authorTextView: TextView = itemView.findViewById(R.id.author)
        val uploadDateTextView: TextView = itemView.findViewById(R.id.upload_date)
        val thumbnailImageView: ImageView = itemView.findViewById(R.id.article_iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_view_article, parent, false)
        return ArticleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articleList[position]
        holder.titleTextView.text = article.title
        holder.authorTextView.text = "author: ${article.author}"
        holder.uploadDateTextView.text = article.uploadDate?.let { formatDate(it) } ?: "N/A"

        Glide.with(holder.thumbnailImageView.context)
            .load(article.thumbnailUrl)
            .apply(RequestOptions().centerCrop())
            .into(holder.thumbnailImageView)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(article.url)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = articleList.size

    fun updateData(newArticleList: List<ArticleResponse>) {
        articleList = newArticleList
        notifyDataSetChanged()
    }

    private fun formatDate(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }
}