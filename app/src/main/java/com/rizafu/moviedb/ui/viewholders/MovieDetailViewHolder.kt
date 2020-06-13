package com.rizafu.moviedb.ui.viewholders

import android.view.View
import coil.transform.BlurTransformation
import com.rizafu.moviedb.R
import com.rizafu.moviedb.databinding.ItemMovieDetailBinding
import com.rizafu.moviedb.utils.BaseViewHolder
import com.rizafu.moviedb.utils.ItemModel
import com.rizafu.moviedb.utils.ViewHolderFactory
import com.rizafu.moviedb.utils.loadImageTMDB

data class MovieDetailItemModel(
    val id: String,
    val imageUrl: String,
    val imageBackdropUrl: String,
    val title: String,
    val subTitle: String,
    val description: String,
    var isFavorite: Boolean = false
) : ItemModel {
    override fun layoutId(): Int = R.layout.item_movie_detail
}

class MovieDetailViewHolder(private val binding: ItemMovieDetailBinding) :
    BaseViewHolder<MovieDetailItemModel>(binding.root) {

    init {
        binding.imageFavorite.addOnItemClick { _, item ->
            //backward of forward
            binding.imageFavorite.speed = if (item.isFavorite) -4f else 2f
            binding.imageFavorite.playAnimation()
            item.isFavorite = item.isFavorite.not()
            item
        }
    }

    override fun bind(item: MovieDetailItemModel) {
        binding.textTitle.text = item.title
        binding.textSubtitle.text = item.subTitle
        binding.textDesc.text = item.description
        binding.imageFavorite.progress = if (item.isFavorite) 1f else 0f
        binding.image.loadImageTMDB(item.imageUrl)
        binding.imageBackdrop.loadImageTMDB(item.imageBackdropUrl) {
            transformations(BlurTransformation(binding.root.context))
        }
    }
}

class MovieDetailViewHolderFactory : ViewHolderFactory {
    override fun layoutId(): Int = R.layout.item_movie_detail

    override fun createViewHolder(viewItem: View): BaseViewHolder<*> =
        MovieDetailViewHolder(ItemMovieDetailBinding.bind(viewItem))

    override fun bindViewHolder(viewHolder: BaseViewHolder<*>, itemModel: ItemModel) {
        (viewHolder as MovieDetailViewHolder).bind(itemModel as MovieDetailItemModel)
    }
}