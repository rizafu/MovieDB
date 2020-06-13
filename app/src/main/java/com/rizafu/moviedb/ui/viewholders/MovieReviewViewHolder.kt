package com.rizafu.moviedb.ui.viewholders

import android.annotation.SuppressLint
import android.view.View
import com.rizafu.moviedb.R
import com.rizafu.moviedb.databinding.ItemMovieReviewBinding
import com.rizafu.moviedb.utils.BaseViewHolder
import com.rizafu.moviedb.utils.ItemModel
import com.rizafu.moviedb.utils.ViewHolderFactory

data class MovieReviewItemModel(
    val id: String,
    val title: String,
    val description: String
) : ItemModel {
    override fun layoutId(): Int = R.layout.item_movie_review
}

class MovieReviewViewHolder(private val binding: ItemMovieReviewBinding) :
    BaseViewHolder<MovieReviewItemModel>(binding.root) {

    @SuppressLint("SetTextI18n")
    override fun bind(item: MovieReviewItemModel) {
        binding.textTitle.text = "${getString(R.string.review_by)} ${item.title}"
        binding.textDesc.text = item.description
    }
}

class MovieReviewViewHolderFactory : ViewHolderFactory {
    override fun layoutId(): Int = R.layout.item_movie_review

    override fun createViewHolder(viewItem: View): BaseViewHolder<*> =
        MovieReviewViewHolder(ItemMovieReviewBinding.bind(viewItem))

    override fun bindViewHolder(viewHolder: BaseViewHolder<*>, itemModel: ItemModel) {
        (viewHolder as MovieReviewViewHolder).bind(itemModel as MovieReviewItemModel)
    }
}