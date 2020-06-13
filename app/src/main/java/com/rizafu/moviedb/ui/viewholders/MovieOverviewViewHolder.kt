package com.rizafu.moviedb.ui.viewholders

import android.view.View
import com.rizafu.moviedb.R
import com.rizafu.moviedb.databinding.ItemMovieOverviewBinding
import com.rizafu.moviedb.utils.BaseViewHolder
import com.rizafu.moviedb.utils.ItemModel
import com.rizafu.moviedb.utils.ViewHolderFactory
import com.rizafu.moviedb.utils.loadImageTMDB

data class MovieOverviewItemModel(
    val id: String,
    val imageUrl: String,
    val title: String,
    val subTitle: String,
    val description: String
) : ItemModel {
    override fun layoutId(): Int = R.layout.item_movie_overview
}

class MovieOverviewViewHolder(private val binding: ItemMovieOverviewBinding) :
    BaseViewHolder<MovieOverviewItemModel>(binding.root) {

    init {
        binding.constraint.addOnItemClick()
    }

    override fun bind(item: MovieOverviewItemModel) {
        binding.textTitle.text = item.title
        binding.textSubtitle.text = item.subTitle
        binding.textDesc.text = item.description
        binding.image.loadImageTMDB(item.imageUrl)
    }
}

class MovieOverviewViewHolderFactory : ViewHolderFactory {
    override fun layoutId(): Int = R.layout.item_movie_overview

    override fun createViewHolder(viewItem: View): BaseViewHolder<*> =
        MovieOverviewViewHolder(ItemMovieOverviewBinding.bind(viewItem))

    override fun bindViewHolder(viewHolder: BaseViewHolder<*>, itemModel: ItemModel) {
        (viewHolder as MovieOverviewViewHolder).bind(itemModel as MovieOverviewItemModel)
    }
}